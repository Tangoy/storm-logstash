package storm.jmx.metrics.consumer;

import java.io.IOException;
import java.lang.reflect.Constructor;
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

public class CustomMetricsConsumer implements IMetricsConsumer {
	public static final Logger LOG = LoggerFactory.getLogger(CustomMetricsConsumer.class);
	private MetricReporter reporter;
	private MetricsProcessing processing;
	private final String STORM_REPORTER = "storm.reporter";
	
	private String stormId;
	
	public void cleanup() {
		// TODO Auto-generated method stub
		if(reporter != null)
			try {
				reporter.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
	}
	
	public void handleDataPoints(TaskInfo taskInfo, Collection<DataPoint> dataPoints) {
		// TODO Auto-generated method stub
		if(!taskInfo.srcComponentId.equalsIgnoreCase("__acker") && !taskInfo.srcComponentId.equalsIgnoreCase("__system")){
			Map<String, Double> maps = Maps.newHashMap();
			try {
				maps = processing.processDataPoints(taskInfo, dataPoints);
				if(maps.size() > 0)
				{
					for(Map.Entry<String, Double> entry : maps.entrySet())
						reporter.sendMetrics(entry.getKey(), entry.getValue());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
		}
	}
	private MetricReporter createInstance(Map config)
	{
		try{
			String className = config.containsKey(STORM_REPORTER)?
					config.get(STORM_REPORTER).toString() :
						"storm.jmx.reporter.JmxMetricReporter";
	
			Class reporterClass = Class.forName(className);
			Constructor<?> constructor = reporterClass.getConstructor(Map.class);
			return (MetricReporter)constructor.newInstance(config);
		}
		catch(Exception se)
		{
			LOG.error(se.getMessage());
			return null;
		}
	}
	private String cleanStormId(String topologyName)
	{
		return topologyName.substring(0, topologyName.substring(0,topologyName.lastIndexOf("-")).lastIndexOf("-"));
	}
	public void prepare(Map config, Object arguments, TopologyContext context, IErrorReporter iErrorReporter) {
		// TODO Auto-generated method stub
		try {
				stormId = cleanStormId(context.getStormId());
				
				Map<Object, Object> mapConfig = Maps.newHashMap();
				mapConfig.putAll(config);
				if(arguments != null && arguments instanceof Map)
			    {
					mapConfig.putAll((Map) arguments);
				}
						
				reporter = createInstance(mapConfig);
				
				if(reporter != null){
					processing = new MetricsProcessing(stormId);
					
					reporter.start();
				}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		catch(Exception se)
		{
			LOG.error(se.getMessage());
		}
		
	}
	
	
}