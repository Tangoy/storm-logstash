package storm.jmx.reporter;

import java.io.IOException;
import java.util.Map;

import info.ganglia.gmetric4j.gmetric.GMetric;
import info.ganglia.gmetric4j.gmetric.GMetricType;
import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;
import info.ganglia.gmetric4j.gmetric.GMetricSlope;
import storm.jmx.metrics.MetricReporter;

public class GangliaMetricReporter extends MetricReporter{
	private final String GANGLIA_HOST = "storm.ganglia.host";
	private final String GANGLIA_PORT = "storm.ganglia.port";
	//private final String GANGLIA_REPORT_PERIOD = "storm.ganglia.period";
	private final String GANGLIA_GROUP = "storm.ganglia.group";
	//private GangliaReporter reporter;
	private GMetric ganglia;
	private String gangliaHost;
	private int gangliaPort;
	private String gangliaGroup = "StormMetrics";
	//private long gangliaPeriod;
	
	public GangliaMetricReporter(Map config)
	{
		super(config);
		processConfig();
	}
	public void processConfig()
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
		//gangliaPeriod = config.containsKey(GANGLIA_REPORT_PERIOD) ?
		//				Long.valueOf(config.get(GANGLIA_REPORT_PERIOD).toString()) :
		//					1;
		try {
			ganglia = new GMetric(gangliaHost, gangliaPort, UDPAddressingMode.MULTICAST,1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ganglia = null;
			LOG.error("Can not create GMetric for Ganglia. " + e.getStackTrace());
		}
	}
	public void registeringMetrics(String name, Double value) throws Exception
	{
		this.announceDouble(name, value, gangliaGroup);
	}
	public void start() throws IOException
	{	
		
		/*if(ganglia!=null){
			reporter = GangliaReporter.forRegistry(METRIC_REGISTRY)
												.convertDurationsTo(TimeUnit.SECONDS)
												.convertRatesTo(TimeUnit.MILLISECONDS)
												.withDMax(10000)
												.withTMax(10000)
												.build(ganglia);
			reporter.start(gangliaPeriod, TimeUnit.SECONDS);
		}*/
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
	public void announceDouble(String name, Double value, String gangliaGroup) throws Exception
	{
		ganglia.announce(name, value, gangliaGroup);			
	}
	public void announceLong(String name, Long value, String gangliaGroup) throws Exception
	{
		ganglia.announce(name, value, gangliaGroup);
	}
	public void announceString(String name, String value,String units, int tmax, int dmax, String gangliaGroup) throws Exception
	{
		ganglia.announce(name, value, GMetricType.STRING, units, GMetricSlope.BOTH, tmax, dmax, gangliaGroup);
	}
	public void announceFloat(String name, Float value, String gangliaGroup) throws Exception
	{
		ganglia.announce(name, value, gangliaGroup);
	}
	public void announceInt(String name, Integer value, String gangliaGroup) throws Exception
	{
		ganglia.announce(name, value, gangliaGroup);
	}
}
