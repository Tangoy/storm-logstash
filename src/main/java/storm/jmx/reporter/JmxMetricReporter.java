package storm.jmx.reporter;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;

import com.codahale.metrics.JmxReporter;

import storm.jmx.metrics.GaugeMetric;
import storm.jmx.metrics.MetricReporter;

public class JmxMetricReporter extends MetricReporter{
	private final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
	private final String DOMAIN_NAME = "domainname";
	private JmxReporter reporter;
	
	private String domainName;
	public JmxMetricReporter(Map config)
	{
		super(config);
		processConfig();
	}
	
	private void processConfig()
	{
		domainName = config.containsKey(DOMAIN_NAME)?
				config.get(DOMAIN_NAME).toString() :
				"storm.jmx.metrics";
		
		
	}
	public void start() throws IOException
	{
		reporter = JmxReporter.forRegistry(METRIC_REGISTRY)
					.registerWith(mBeanServer)
					.inDomain(domainName)
					.convertDurationsTo(TimeUnit.MILLISECONDS)
					.convertRatesTo(TimeUnit.SECONDS).build();
		reporter.start();
	}
	public void stop()
	{
		if(reporter != null)
			reporter.stop();
	}
}
