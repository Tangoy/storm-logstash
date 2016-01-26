package storm.reporter;

import java.lang.management.ManagementFactory;

import java.util.Map;

import javax.management.MBeanServer;

import com.codahale.metrics.DefaultObjectNameFactory;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ObjectNameFactory;

import storm.metrics.AbstractMetricReporter;
import storm.metrics.GaugeMetric;

public class JmxMetricReporter extends AbstractMetricReporter{
	private final MetricRegistry METRIC_REGISTRY = new MetricRegistry();
	private final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
	
	
	private JmxReporter reporter;
	private String domainName;
	
	public JmxMetricReporter(){}
	
	public JmxMetricReporter(Map config){
		this.setConfig(config);
		
	}
	
	public void start()
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
	
	public void sendMetrics(String name, Double value){
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

	@Override
	public void setConfig(Map config) {
		// TODO Auto-generated method stub
		this.config = config;
		this.processConfig();
	}
}
