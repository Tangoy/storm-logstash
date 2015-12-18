package storm.jmx.metrics;

import java.util.Map;


public abstract class AbstractMetricReporter {
	protected final String GANGLIA_HOST = "storm.ganglia.host";
	protected final String GANGLIA_PORT = "storm.ganglia.port";
	protected final String GANGLIA_GROUP = "storm.ganglia.group";
	
	protected final String GRAPHITE_HOST = "storm.graphite.host";
	protected final String GRAPHITE_PORT = "storm.graphite.port";
	protected final String GRAPHITE_PROTOCOL = "storm.graphite.protocol";
	
	protected final String DOMAIN_NAME = "storm.jmx.domain";
	
	protected final String UDP_IP_ADDRESS = "storm.udp.ipaddress";
	protected final String UDP_PORT = "storm.udp.port";
	
	protected final String TCP_IP_ADDRESS = "storm.tcp.ipaddress";
	protected final String TCP_PORT = "storm.tcp.port";
	
	protected Map config;
	
	protected abstract void processConfig();
	
	public abstract void setConfig(Map config);
	public abstract void sendMetrics(String name, Double value);
	
	public abstract void start();

	public abstract void stop();
}
