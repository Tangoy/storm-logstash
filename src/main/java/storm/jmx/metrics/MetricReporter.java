package storm.jmx.metrics;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;

public class MetricReporter {
	public static final Logger LOG = LoggerFactory.getLogger(MetricReporter.class);
	protected final MetricRegistry METRIC_REGISTRY = new MetricRegistry();
	protected Map config;
	public MetricReporter(Map config)
	{
		this.config = config;
	}
	public void setConfig(Map config)
	{
		this.config = config;
	}
	public void processCongif()
	{
		
	}
	public void registeringMetrics(String name, Double value) throws Exception
	{
		
	}
	public void start() throws IOException
	{
		
	}
	public void stop()
	{
		
	}
}
