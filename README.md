# storm-jmx-metrics

An project is to get all storm built-in metrics and send to Logstash-forwarder by Jmx, Ganglia or Graphite. 
The idea came up with an open source project named storm-graphite (https://github.com/verisign/storm-graphite)

This project used Coda Hale metrics (http://metrics.dropwizard.io) and deployed IMetricsConsumer of Storm.

To use this:
- Setup on local mode (I just test on this)
- Add lines in $STORM_HOME/conf/storm.yaml file:
```
  topology.metrics.consumer.register:
    - class: "storm.jmx.metrics.consumer.CustomMetricsConsumer"
  ```
- To report to Jmx, just put parameters in $STORM_HOME/conf/storm.yaml or Config in topology:
```  
   argument:
    - storm.reporter: "storm.jmx.reporter.JmxMetricRepoter"
    - storm.domain.name: "storm.metrics"
```
- To report to Ganglia, just put parameters in $STORM_HOME/conf/storm.yaml or Config in topology
```
   argument:
    - storm.reporter: "storm.jmx.reporter.GangliaMetricRepoter"
    - storm.ganglia.host: "localhost"
    - storm.ganglia.port: 8649
```
- To report to Graphite (not tested yet), put parameters in $STORM_HOME/conf/storm.yaml or Config in topology:
```
 argument:
	- storm.reporter: "storm.jmx.reporter.GraphiteRepoter"
	- storm.graphite.host: "localhost"
	- storm.graphite.port: 2003
	- storm.graphite.protocol: "UDP"
```	
- And finally, To send metrics to Logstash with Jmx input.
   - First, install Jmx Plugin in Logstash
   - Create pipeline:
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
   stdout{codec=>rubbydebug}
}
 ```
   - Create json file to query remote object:
  ```
  {
  "host" : "localhost"
  "port" : "16703"
  "querries" :[
    {
      "object_name" : "storm.metrics.Gauge:name=*",
      "attributes" : ["Value"]
    } ]
}
  ```
 
*NOTE: 
   - In object_name field of json file, storm.metrics is your domain name when configuring in storm.yaml
   - Make sure that you enable jmx in your storm. Add those lines in storm.yaml
   ```
   Worker:
 worker.childopts: " -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=1%ID%  -Djava.rmi.server.hostname=<IP_ADRESS/HOST_NAME>"

   Supervisor
supervisor.childopts: " -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=any_open_port_number -Djava.rmi.server.hostname=<IP_ADRESS/HOST_NAME>"
   Nimbus
 nimbus.childopts: " -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=any_open_port_number -Djava.rmi.server.hostname=<IP_ADRESS/HOST_NAME>"
   ```
- To send metrics to Logstash with Ganglia and Graphite input, just create pipeline in Logstash.
