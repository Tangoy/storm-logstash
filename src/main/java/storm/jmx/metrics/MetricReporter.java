package storm.jmx.metrics;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;

public class MetricReporter {
	public static final Logger LOG = LoggerFactory.getLogger(MetricReporter.class);
	
	private final String STORM_REPORTER = "storm.reporter";
	protected Map config;
	
	public MetricReporter(Map config)
	{
		this.config = config;
	}
	public void setConfig(Map config)
	{
		this.config = config;
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
