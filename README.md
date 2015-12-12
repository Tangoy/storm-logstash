# storm-jmx-metrics

An project is to get all storm built-in metrics and send to Jmx, Ganglia. The idea came up with an open source project named storm-graphite (https://github.com/verisign/storm-graphite)

This project used Coda Hale metrics (http://metrics.dropwizard.io) and deployed IMetricsConsumer of Storm.

To use this:
- Setup on local mode (I just test on this)
- Add lines on storm.yaml file: 
    topology.metrics.consumer.register:
    - class: "storm.jmx.metrics.consumer.CustomMetricsConsumer"
- To report to JmxReporter just put parameter in storm.yaml or config in topology
    argument:
    - reperter: JmxReporter
    - domainname: storm.jmx.metrics
- To report to Ganglia just put parameter in storm.yaml or config in topology
    - reporter: GangliaReporter

Future work:
- Support to send metrics to Logstash
- Support to send metrics to Graphite
