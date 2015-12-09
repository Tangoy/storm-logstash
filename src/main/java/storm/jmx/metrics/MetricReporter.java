package storm.jmx.metrics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

import backtype.storm.metric.api.IMetricsConsumer.DataPoint;
import backtype.storm.metric.api.IMetricsConsumer.TaskInfo;

public class MetricReporter {
	public static final Logger LOG = LoggerFactory.getLogger(MetricReporter.class);
	
	private MetricRegistry registry;
	private Map<String, Double> maps;
	
	public MetricReporter()
	{
		registry = new MetricRegistry();
		maps = new HashMap<String, Double>();
		
	}
	private void registeringMetrics()
	{
		for(final String key : maps.keySet())
		{
			Gauge<Double> g = new Gauge<Double>() {
				
				public Double getValue() {
					// TODO Auto-generated method stub
					return maps.get(key);
				}
			};
			if(!registry.getGauges().containsKey(key))
				registry.register(key, g);
		}
	}
	public void processDataPoints(TaskInfo taskInfo, Collection<DataPoint> dataPoints)
	{
		for(DataPoint p : dataPoints)
		{
			if(p.value instanceof Map){
				try
				{
					Map<String, Object> map = (Map<String, Object>) p.value;
					for(Map.Entry<String, Object> entry : map.entrySet())
					{
						Double value = Double.valueOf(entry.getValue().toString());   
						if(value != null)
						{
							StringBuffer name = new StringBuffer(p.name).append(".")
									.append(entry.getKey().toString());
							maps.put(this.processingMetricPrefix(name.toString(), taskInfo), value);
						}
					}
				}catch(Exception se)
				{
					LOG.error(se.getMessage() + "\n" + se.getStackTrace());
				}
			}
			else if(p.value instanceof Number)
			{
				Double value = 0.00;
				try
				{
					value = ((Number)p.value).doubleValue();
				}catch(Exception se){
					LOG.error(se.getMessage() + "\n" + se.getStackTrace());
				}
				maps.put(processingMetricPrefix(p.name, taskInfo), value);
			}
		}
		this.registeringMetrics();
	}
	public void registerJmxReporter()
	{
		final JmxReporter reporter = JmxReporter.forRegistry(registry).build();
		
		reporter.start(); 
	}
	private String processingMetricPrefix(String name, TaskInfo taskInfo)
	{
		if(name != null)
		{
			String str = (taskInfo.srcComponentId + "." 
					+ taskInfo.srcWorkerHost + "." 
					+ taskInfo.srcTaskId
					+ "." + name).replaceAll("_", "")
					.replaceAll(":", "")
					.toString();
			
			return str;
		}
		return "";
	}
}
