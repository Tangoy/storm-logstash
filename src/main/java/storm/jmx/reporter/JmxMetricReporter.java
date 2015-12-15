package storm.jmx.reporter;

import java.lang.management.ManagementFactory;

import java.util.Map;

import javax.management.MBeanServer;

import com.codahale.metrics.DefaultObjectNameFactory;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ObjectNameFactory;

import storm.jmx.metrics.GaugeMetric;
import storm.jmx.metrics.MetricReporter;

public class JmxMetricReporter extends MetricReporter{
	protected final MetricRegistry METRIC_REGISTRY = new MetricRegistry();
	private final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
	private final String DOMAIN_NAME = "storm.domainname";
	private JmxReporter reporter;
	
	private String domainName;
	public JmxMetricReporter(Map config){
		super(config);
		this.processConfig();
	}
	
	public void start() throws Exception
	{
		ObjectNameFactory objectname = new DefaultObjectNameFactory();
		objectname.createName("type",domainName,"gauge");
		reporter = JmxReporter.forRegistry(METRIC_REGISTRY)
					.registerWith(mBeanServer)
					.inDomain(domainName)
					.createsObjectNamesWith(objectname)
					.build();
		
		reporter.start();
	}
	public void stop()
	{
		if(reporter != null)
			reporter.stop();
	}

	protected void processConfig() {
		// TODO Auto-generated method stub
		domainName = config.containsKey(DOMAIN_NAME)?
				config.get(DOMAIN_NAME).toString() :
				"storm.jmx.metrics";
	}
	
	public void sendMetrics(String name, Double value) throws Exception {
		// TODO Auto-generated method stub
		
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
}
