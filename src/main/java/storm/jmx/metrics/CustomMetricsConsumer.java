package storm.jmx.metrics;

import java.util.Collection;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.metric.api.IMetricsConsumer;
import backtype.storm.task.IErrorReporter;
import backtype.storm.task.TopologyContext;

public class CustomMetricsConsumer implements IMetricsConsumer {
	public static final Logger LOG = LoggerFactory.getLogger(CustomMetricsConsumer.class);
	private MetricReporter reporter;
	
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	
	public void handleDataPoints(TaskInfo taskInfo, Collection<DataPoint> dataPoints) {
		// TODO Auto-generated method stub
		reporter.processDataPoints(taskInfo, dataPoints);		
	}
	public void prepare(Map map, Object object, TopologyContext context, IErrorReporter iErrorReporter) {
		// TODO Auto-generated method stub
		reporter = new MetricReporter();
		reporter.registerJmxReporter();
	}
	
	
}