package storm.jmx.metrics.consumer;

import java.io.IOException;
import java.util.Collection;

import java.util.Map;

import org.apache.storm.guava.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.metric.api.IMetricsConsumer;
import backtype.storm.task.IErrorReporter;
import backtype.storm.task.TopologyContext;
import storm.jmx.metrics.MetricReporter;
import storm.jmx.metrics.MetricsProcessing;
import storm.jmx.reporter.GangliaMetricReporter;
import storm.jmx.reporter.JmxMetricReporter;


public class CustomMetricsConsumer implements IMetricsConsumer {
	public static final Logger LOG = LoggerFactory.getLogger(CustomMetricsConsumer.class);
	private MetricReporter reporter;
	private MetricsProcessing processing;
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	
	public void handleDataPoints(TaskInfo taskInfo, Collection<DataPoint> dataPoints) {
		// TODO Auto-generated method stub
		Map<String, Double> maps = Maps.newHashMap();
		try {
			maps = processing.processDataPoints(taskInfo, dataPoints);
			if(maps.size() > 0)
			{
				for(Map.Entry<String, Double> entry : maps.entrySet())
					reporter.registeringMetrics(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
	}
	public void prepare(Map config, Object object, TopologyContext context, IErrorReporter iErrorReporter) {
		// TODO Auto-generated method stub
		String reporterConfig = (config.containsKey("REPORTER")) ? config.get("REPORTER").toString() : "JmxReporter";
		if(reporterConfig.equals("JmxReporter"))
		{
			reporter = new JmxMetricReporter(config);
		}
		else
			reporter = new GangliaMetricReporter();
		
		processing = new MetricsProcessing();
		
		try {
			reporter.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
	}
	
	
}