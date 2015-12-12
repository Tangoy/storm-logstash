package storm.jmx.metrics;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;

public class MetricReporter {
	public static final Logger LOG = LoggerFactory.getLogger(MetricReporter.class);
	protected final MetricRegistry METRIC_REGISTRY = new MetricRegistry();
		
	public MetricReporter()
	{
		
	}
	public void registeringMetrics(String name, Double value) throws Exception
	{
		if(!METRIC_REGISTRY.getGauges().containsKey(name))
		{
			GaugeMetric<Double> gauge = new GaugeMetric<Double>();
			gauge.setValue(value);
			METRIC_REGISTRY.register(name, gauge);
		}
		else
		{
			GaugeMetric<Double> gauge = (GaugeMetric<Double>)METRIC_REGISTRY.getGauges().get(name);
			gauge.setValue(value);
		}
	}
	public void start() throws IOException
	{
		
	}
	
}
