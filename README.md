# storm-jmx-metrics

An project is to get all storm built-in metrics and export to Jmx. The idea came up with an open source project named storm-graphite (https://github.com/verisign/storm-graphite)

This project used Coda Hale metrics (http://metrics.dropwizard.io) and deployed IMetricsConsumer of Storm.

To use this:
- Setup on local mode (I just test on this)
- Add package storm.jmx.metrics into a storm project
- Add lines on storm.yaml file: 
    topology.metrics.consumer.register:
    - class: "storm.jmx.metrics.CustomMetricsConsumer"
- Open jconsole to see all the metrics on worker
- Or use jmxtrans to send json files metrics to Ganglia or something else.
