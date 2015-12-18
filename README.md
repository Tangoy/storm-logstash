# storm-logstash-metrics

An project is to get all built-in metrics of storm and send directly to logstash-forwarder with Jmx, Ganglia, Graphite, TCP and UDP logstash-input.
Although my purpose is to use logstash-input-plugin, the program still works well with Ganglia and Graphite monitoring system.

The idea came up with an open source project named storm-graphite (https://github.com/verisign/storm-graphite).
This project used Coda Hale metrics (http://metrics.dropwizard.io) and deployed IMetricsConsumer of Storm.

##Usage
- Setup on local mode (I just test on this)

###Enable Metrics Consumer
- Add lines in *$STORM_HOME/conf/storm.yaml* file:
```
  topology.metrics.consumer.register:
    - class: "storm.jmx.metrics.consumer.CustomMetricsConsumer"
  ```
### JMX reporter
- To report to Jmx, just put parameters in *$STORM_HOME/conf/storm.yaml* or *Config* in topology:
```  
   argument:
    - storm.reporter: "storm.jmx.reporter.JmxMetricRepoter"
    - storm.domainname: "storm.metrics"
    - storm.jmx.domain: "MBEAN_DOMAIN_NAME"
```
### Ganglia reporter
- To report to Ganglia, just put parameters in *$STORM_HOME/conf/storm.yaml* or *Config* in topology
```
   argument:
    - storm.reporter: "storm.jmx.reporter.GangliaMetricRepoter"
    - storm.ganglia.host: "HOST_IP"
    - storm.ganglia.port: PORT 		//default = 8649
    - storm.ganglia.group: "GANGLIA_GROUP"
```
### Graphite reporter
- To report to Graphite, put parameters in *$STORM_HOME/conf/storm.yaml* or *Config* in topology:
```
 argument:
	- storm.reporter: "storm.jmx.reporter.GraphiteRepoter"
	- storm.graphite.host: "HOST_IP"
	- storm.graphite.port: PORT		//default = 2003
```	
### Logstash Configuration
- Send metrics to Logstash with *Jmx input*.
   - First, install **logstash-input-jmx** in Logstash-fowarder
   - Create pipeline: Example
  ```
 input{
   jmx{
       path=>"path-to-json-file"
       polling_frequency=>15
       type=>"jmx"
       nb_thread=>1
   }
}

output{
   elasticsearch{ hosts=>["localhost:9200"]}
   stdout{codec=>rubydebug}
}
 ```
   - Json file to query remote object:
  ```
  {
  "host" : "localhost"
  "port" : "16703"
  "querries" :[
    {
      "object_name" : "storm.metrics:name=*",
      "attributes" : ["Value"]
    } ]
}
  ```
 
*NOTE: 
   - In *object_name* field of json file, *storm.metrics* is your domain name when configuring in *storm.yaml*
   - Make sure that you **enable jmx** in your storm. Add those lines in *storm.yaml*
   
   ```
   Worker:
 worker.childopts: " -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=1%ID%  -Djava.rmi.server.hostname=<IP_ADRESS/HOST_NAME>"
   Supervisor
supervisor.childopts: " -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=any_open_port_number -Djava.rmi.server.hostname=<IP_ADRESS/HOST_NAME>"
   Nimbus
 nimbus.childopts: " -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=any_open_port_number -Djava.rmi.server.hostname=<IP_ADRESS/HOST_NAME>"
   ```

- To send metrics to Logstash with Ganglia and Graphite input, just create pipeline in Logstash.
  Example
```
 input{
   ganlia{
       host=>"localhost"
       port=>8649
   }
   graphite{
   	   host=>"localhost"
   	   port=>2003
   }
}

output{
   elasticsearch{ hosts=>["localhost:9200"]}
   stdout{codec=>rubbydebug}
}
 ```
- Pipeline in logstash if using UDP or TCP:
```
	input{
	 tcp{
	  host=>"localhost"
	  port=>14445
	  mode=>"server"
	  ssl_verify=>false
	 }
	 udp{
 	 host=>"localhost"
      port=>14446
     }
   }
	output{
	 stdout{codec=>rubydebug}
	}
```

