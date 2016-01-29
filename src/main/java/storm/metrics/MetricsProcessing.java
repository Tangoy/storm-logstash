package storm.metrics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.metric.api.IMetricsConsumer.DataPoint;
import backtype.storm.metric.api.IMetricsConsumer.TaskInfo;


public class MetricsProcessing {
	public static final Logger LOG = LoggerFactory.getLogger(MetricsProcessing.class);
	
	public static Map<String,Double> processDataPoints(String stormId, TaskInfo taskInfo, Collection<DataPoint> dataPoints)
	{
		Map<String, Double> maps = new HashMap<String, Double>();
		for(DataPoint p : dataPoints)
		{
			if(p.value == null)
				continue;
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
							if(!MetricFilter.isFilter(name.toString()))
								maps.put(processingMetricPrefix(stormId, name.toString(), taskInfo), value);
						}
					}
				}catch(Exception e)
				{
					LOG.error(e.getMessage());
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
				maps.put(processingMetricPrefix(stormId, p.name, taskInfo), value);
			}
		}
		return maps;
	}
	private static String processingMetricPrefix(String stormId, String name, TaskInfo taskInfo)
	{
		
		if(name != null)
		{
			StringBuffer strBuff = new StringBuffer("");
			strBuff.append(taskInfo.srcWorkerHost).append(":")
					.append(taskInfo.srcWorkerPort).append(".")
					.append(stormId).append(".")
					.append(taskInfo.srcComponentId.replaceAll("__", "")).append(".")
					.append(taskInfo.srcTaskId).append(".")
					.append(name.replaceAll("__", ""));				
			return strBuff.toString();
		}
		return "";
	}
}
