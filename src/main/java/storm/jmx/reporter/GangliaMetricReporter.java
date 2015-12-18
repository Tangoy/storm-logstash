package storm.jmx.reporter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ganglia.GangliaReporter;

import info.ganglia.gmetric4j.gmetric.GMetric;
import info.ganglia.gmetric4j.gmetric.GMetricType;
import info.ganglia.gmetric4j.gmetric.GangliaException;
import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;
import info.ganglia.gmetric4j.gmetric.GMetricSlope;
import storm.jmx.metrics.AbstractMetricReporter;
import storm.jmx.metrics.GaugeMetric;

public class GangliaMetricReporter extends AbstractMetricReporter{
	public static final Logger LOG = LoggerFactory.getLogger(GangliaMetricReporter.class);
	protected final MetricRegistry METRIC_REGISTRY = new MetricRegistry();
	
	private GMetric ganglia;
	private String gangliaHost;
	private int gangliaPort;
	private String gangliaGroup = "StormMetrics";
	
	public GangliaMetricReporter()
	{}
	public GangliaMetricReporter(Map config)
	{
		this.setConfig(config);
	}
	
	protected void processConfig()
	{
		gangliaHost = config.containsKey(GANGLIA_HOST) ? 
				config.get(GANGLIA_HOST).toString() :
					"localhost";
		gangliaPort = config.containsKey(GANGLIA_PORT) ?
				Integer.valueOf(config.get(GANGLIA_PORT).toString()) :
					8649;
		gangliaGroup = config.containsKey(GANGLIA_GROUP) ?
				config.get(GANGLIA_GROUP).toString() :
					"StormMetrics";
		
	}
	public void sendMetrics(String name, Double value)
	{
		if(ganglia != null)
			this.announceDouble(name, value, gangliaGroup);
	}
	public void start()
	{	
		try {
			ganglia = new GMetric(gangliaHost, gangliaPort, UDPAddressingMode.MULTICAST,1,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ganglia = null;
			LOG.error(e.getMessage());
		}
	}
	public void stop()
	{
		if(ganglia != null)
			try {
				ganglia.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
	}
	public void announceDouble(String name, Double value, String gangliaGroup)
	{
		try {
			ganglia.announce(name, value, gangliaGroup);
		} catch (GangliaException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}			
	}
	public void announceLong(String name, Long value, String gangliaGroup)
	{
		try {
			ganglia.announce(name, value, gangliaGroup);
		} catch (GangliaException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
	}
	public void announceString(String name, String value,String units, int tmax, int dmax, String gangliaGroup)
	{
		try {
			ganglia.announce(name, value, GMetricType.STRING, units, GMetricSlope.BOTH, tmax, dmax, gangliaGroup);
		} catch (GangliaException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
	}
	public void announceFloat(String name, Float value, String gangliaGroup)
	{
		try{
			ganglia.announce(name, value, gangliaGroup);
		}
		catch(GangliaException e){
			LOG.error(e.getMessage());
		}
	}
	public void announceInt(String name, Integer value, String gangliaGroup)
	{
		try {
			ganglia.announce(name, value, gangliaGroup);
		} catch (GangliaException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
	}
	public void setConfig(Map config) {
		// TODO Auto-generated method stub
		this.config = config;
		this.processConfig();
	}
}
