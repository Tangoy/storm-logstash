package storm.jmx.reporter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ganglia.GangliaReporter;

import info.ganglia.gmetric4j.gmetric.GMetric;
import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;
import storm.jmx.metrics.GaugeMetric;
import storm.jmx.metrics.MetricReporter;

public class GangliaMetricReporter extends MetricReporter{
	private final String GANGLIA_HOST = "storm.ganglia.host";
	private final String GANGLIA_PORT = "storm.ganglia.port";
	private final String GANGLIA_REPORT_PERIOD = "ganlia.period";
	
	private GangliaReporter reporter;
	private GMetric ganglia;
	private String gangliaHost;
	private int gangliaPort;
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
		ganglia.announce(name, value, "StormMetrics");
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
}
