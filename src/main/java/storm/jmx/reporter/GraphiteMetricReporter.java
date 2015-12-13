package storm.jmx.reporter;

import java.net.InetSocketAddress;
import java.util.Map;

import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteSender;
import com.codahale.metrics.graphite.GraphiteUDP;

import storm.jmx.metrics.MetricReporter;

public class GraphiteMetricReporter extends MetricReporter {
	
	private final String GRAPHITE_HOST = "storm.graphite.host";
	private final String GRAPHITE_PORT = "storm.graphite.port";
	private final String GRAPHITE_PROTOCOL = "storm.graphite.protocol";
	
	private String graphiteHost;
	private int graphitePort;
	private String graphiteProtocol;
	
	private InetSocketAddress inetSocketAddress;
	private GraphiteSender graphite;
	public GraphiteMetricReporter(Map config) {
		super(config);
		// TODO Auto-generated constructor stub
		this.processConfig();
	}	
	public void sendMetrics(String name, Double value) throws Exception
	{
		if(!graphite.isConnected())
			graphite.connect();
		graphite.send(name, String.format("%8.2f", value), System.currentTimeMillis());
		graphite.flush();
	}
	public void start() throws Exception
	{
			inetSocketAddress = new InetSocketAddress(graphiteHost, graphitePort);
			if(graphiteProtocol.equalsIgnoreCase("UDP"))
				graphite = new GraphiteUDP(inetSocketAddress);
			else
				graphite = new Graphite(inetSocketAddress);
			graphite.connect();
	}
	public void stop() throws Exception
	{
		if(graphite.isConnected())
			graphite.close();
	}
	@Override
	protected void processConfig() {
		// TODO Auto-generated method stub
		graphiteHost = config.containsKey(GRAPHITE_HOST) ? 
				config.get(GRAPHITE_HOST).toString() :
					"localhost";
		graphitePort = config.containsKey(GRAPHITE_PORT) ?
				Integer.valueOf(config.get(GRAPHITE_PORT).toString()) :
					2003;
		graphiteProtocol = config.containsKey(GRAPHITE_PROTOCOL) ?
				config.get(GRAPHITE_PROTOCOL).toString():
				"TCP";
	}
}
