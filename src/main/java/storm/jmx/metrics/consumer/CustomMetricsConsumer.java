package storm.jmx.metrics.consumer;

import java.util.Collection;
import java.util.Map;

import org.apache.storm.guava.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.metric.api.IMetricsConsumer;
import backtype.storm.task.IErrorReporter;
import backtype.storm.task.TopologyContext;
import storm.jmx.metrics.AbstractMetricReporter;
import storm.jmx.metrics.MetricsProcessing;


public class CustomMetricsConsumer implements IMetricsConsumer {
	public static final Logger LOG = LoggerFactory.getLogger(CustomMetricsConsumer.class);
	private AbstractMetricReporter reporter;
	
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
				maps = MetricsProcessing.processDataPoints(stormId, taskInfo, dataPoints);
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
	private <T extends AbstractMetricReporter> T createInstance(Map config)
	{
		try {
			String className = config.containsKey(STORM_REPORTER)?
					config.get(STORM_REPORTER).toString() :
						"storm.jmx.reporter.JmxMetricReporter";
			T r = (T)Class.forName(className).newInstance();
			r.setConfig(config);
			return r;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
			return null;
		}
	}
	private String cleanStormId(String topologyName)
	{
		return topologyName.substring(0, topologyName.substring(0,topologyName.lastIndexOf("-")).lastIndexOf("-"));
	}
	public void prepare(Map config, Object arguments, TopologyContext context, IErrorReporter iErrorReporter) {
		// TODO Auto-generated method stub
		try{
			stormId = cleanStormId(context.getStormId());
			
			Map<Object, Object> mapConfig = Maps.newHashMap();
			mapConfig.putAll(config);
			if(arguments != null && arguments instanceof Map)
		    {
				mapConfig.putAll((Map) arguments);
			}
			String className = config.containsKey(STORM_REPORTER)?
					config.get(STORM_REPORTER).toString() :
						"storm.jmx.reporter.JmxMetricReporter";
			reporter = createInstance(config);
			
			reporter.start();
		}
		catch(Exception e)
		{
			LOG.error(e.getMessage());
		}
	}
}