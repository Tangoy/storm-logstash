package storm.jmx.metrics;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricReporter {
	public static final Logger LOG = LoggerFactory.getLogger(MetricReporter.class);
	
	protected Map config;
	public MetricReporter(Map config)
	{
		this.config = config;
	}
	protected  void processConfig(){}
	public  void sendMetrics(String name, Double value) throws Exception{}
	
	public void start() throws Exception{}

	public void stop() throws Exception{}
}
