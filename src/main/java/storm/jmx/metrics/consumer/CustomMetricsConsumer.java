package storm.jmx.metrics.consumer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
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
	private final String STORM_REPORTER = "storm.reporter";
	public void cleanup() {
		// TODO Auto-generated method stub
		reporter.stop();
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
	public void prepare(Map config, Object arguments, TopologyContext context, IErrorReporter iErrorReporter) {
		// TODO Auto-generated method stub
		//if(arguments != null)
		//	config.putAll((Map) arguments);
		Map<Object, Object> mapConfig = Maps.newHashMap();
		mapConfig.putAll(config);
		if(arguments != null && arguments instanceof Map)
	    {
			Map<Object, Object> map = (Map<Object, Object>) arguments;
			mapConfig.putAll(map);
		}
		
		String strReport =  config.get(STORM_REPORTER).toString();
		
		if(strReport.compareToIgnoreCase("jmx") == 0)
		{
			reporter = new JmxMetricReporter(config);
		}
		else if(strReport.compareToIgnoreCase("ganglia") == 0)
			reporter = new GangliaMetricReporter(config);
		//reporter = new GangliaMetricReporter(config);
		processing = new MetricsProcessing();
		
		try {
			reporter.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
	}
	
	
}