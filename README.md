# storm-logstash-metrics

An project is to get all built-in metrics of storm and send directly to logstash with Jmx, Ganglia, Graphite, TCP and UDP logstash-input.
Although my purpose is for logstash with input-plugins, the program still works well with Ganglia and Graphite monitoring system and Jmx.

The idea came up with an open source project named storm-graphite (https://github.com/verisign/storm-graphite).
This project used Coda Hale metrics (http://metrics.dropwizard.io) and deployed IMetricsConsumer of Storm.

##Usage
- Testing on cluster mode
- First, Copy jar file: *storm-metrics-0.0.1-SNAPSHOT-jar-with-dependencies.jar* to $STORM_HOME/lib.
- Then Enable Metrics Consumer <SEE BELOW>

###Enable Metrics Consumer
- Add lines in *$STORM_HOME/conf/storm.yaml* file:
```
  topology.metrics.consumer.register:
    - class: "storm.metrics.consumer.CustomMetricsConsumer"
  ```
  Or when defining topology, add this line:
  ```
  	Config conf = new Config();
  	conf.registrerMetricsConsumer(storm.metrics.consumer.CustomMetricsConsumer.class);
  ```
  
### JMX reporter
- To report to Jmx, just put parameters in *$STORM_HOME/conf/storm.yaml* or *Config* in topology:
```  
   argument:
    - storm.reporter: "storm.reporter.JmxMetricReporter"
    - storm.jmx.domain: "MBEAN_DOMAIN_NAME"
```
```
	conf.add("storm.reporter","storm.reporter.JmxMetricReporter");
	conf.add("storm.jmx.domain", "MBEAN_DOMAIN_NAME");
```

### Ganglia reporter
- To report to Ganglia, just put parameters in *$STORM_HOME/conf/storm.yaml* or *Config* in topology
```
   argument:
    - storm.reporter: "storm.reporter.GangliaMetricReporter"
    - storm.ganglia.host: "HOST_IP"
    - storm.ganglia.port: PORT 		//default = 8649
    - storm.ganglia.group: "GANGLIA_GROUP"
```
```
	conf.add("storm.reporter","storm.reporter.GangliaMetricReporter");
	conf.add("storm.ganglia.host", "HOST_IP");
	conf.add("storm.ganglia.port", PORT);
	conf.add("storm.ganglia.group", "GANGLIA_GROUP");
```

### Graphite reporter
- To report to Graphite, put parameters in *$STORM_HOME/conf/storm.yaml* or *Config* in topology:
```
 argument:
	- storm.reporter: "storm.reporter.GraphiteMetricReporter"
	- storm.graphite.host: "HOST_IP"
	- storm.graphite.port: PORT		//default = 2003
```	
```
	conf.add("storm.reporter","storm.reporter.GraphiteReporter");
	conf.add("storm.graphite.host", "HOST_IP");
	conf.add("storm.graphite.port", PORT);
```

### TCP/UDP configuration 
 - With TCP/UDP reporter

 ```
 argument for UDP reporter:
	- storm.reporter: "storm.reporter.UDPMetricReporter"
	- storm.udp.host: "HOST_IP"
	- storm.udp.port: PORT		//default = 1446
```	
```
	conf.add("storm.reporter","storm.reporter.UDPMetricReporter");
	conf.add("storm.udp.ipaddress", "HOST_IP");
	conf.add("storm.udp.port", PORT);
```
```
argument for TCP reporter:
	- storm.reporter: "storm.reporter.TCPMetricReporter"
	- storm.tcp.host: "HOST_IP"
	- storm.tcp.port: PORT		//default = 1445
```
 ```
	conf.add("storm.reporter","storm.reporter.TCPMetricReporter");
	conf.add("storm.tcp.ipaddress", "HOST_IP");
	conf.add("storm.tcp.port", PORT);
```

### Logstash Configuration
- Send metrics to Logstash with *Jmx input*.
   - First, install **logstash-input-jmx** in logstash
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
filter{  #### SEE BELOW }
output{
   elasticsearch{ hosts=>["HOST:PORT"]}
   stdout{codec=>rubydebug}
}
 ```
   - Json file to query remote object:
  ```
  {
  "host" : "JVM_HOST"
  "port" : "JVM_PORT"
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
supervisor.childopts: " -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=<any_open_port_number> -Djava.rmi.server.hostname=<IP_ADRESS/HOST_NAME>"
   Nimbus
 nimbus.childopts: " -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=<any_open_port_number> -Djava.rmi.server.hostname=<IP_ADRESS/HOST_NAME>"
   ```

- To send metrics to Logstash with Ganglia and Graphite input, just create pipeline in Logstash.
  Example
```
 input{
   ganlia{
       host=>"HOST" #default localhost
       port=>PORT	#default 8649
   }
   graphite{
   	   host=>"HOST" #default localhost
   	   port=>PORT	#default 2003
   }
}
filter{  #### SEE BELOW }
output{
   elasticsearch{ hosts=>"HOST:PORT"}
   stdout{codec=>rubydebug}
}
 ```
- Pipeline in logstash if using UDP or TCP:
```
	input{
	 tcp{
	  host=>"IP_SERVER" #default localhost
	  port=>PORT		#default: 14445
	  mode=>"server"
	  ssl_verify=>false
	 }
	 udp{
 	 host=>"HOST"       #default localhost
      port=> PORT		#default: 14446
     }
   }
   filter{  #### SEE BELOW }
	output{
	elasticsearch{ hosts=>"HOST:PORT"}
	 stdout{codec=>rubydebug}
	}
```
* Structure of metric name: {HOST}:{PORT}.{TOPOLOGY}.{COMPONENT}.{TASKID}.{METRIC.EXTRA_INFORMATION}

  for example:
  
  			Storm-cluster:6700.Local-Storm-Example.spout-example.5.transfer-count.metrics
  			
* Filter for logstash if using UDP/TCP reporter
  
```
filter{
 grok{
  break_on_match=>false
  patterns_dir=>"patterns.conf"
  match=>{"message" => "Name: %{HOSTNAME:host}:%{INT:port}.%{COMPONENTNAME:topologyname}.%{COMPONENTNAME:componentid}.%{INT:taskid}.%{METRICNAME:metricname} Value: %{NUMBER:value}"}
  remove_field=>["message"]
  overwrite=>["host","port"]
 }
}
```

Custom metric to extract metric name in storm_patterns.conf

``
COMPONENTNAME [a-zA-Z0-9_-]+
METRICNAME [a-zA-Z0-9/._-]+
``
