package storm.reporter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.graphite.Graphite;

import storm.metrics.AbstractMetricReporter;


public class GraphiteMetricReporter extends AbstractMetricReporter {
	public static final Logger LOG = LoggerFactory.getLogger(GraphiteMetricReporter.class);
	
	private String graphiteHost;
	private int graphitePort;
	
	private InetSocketAddress inetSocketAddress;

	private Graphite graphite;
	
	public GraphiteMetricReporter()
	{}
	public GraphiteMetricReporter(Map config) {
		
		// TODO Auto-generated constructor stub
		this.setConfig(config);
	}	
	public void sendMetrics(String name, Double value)
	{
		try {
			if(!graphite.isConnected())
				graphite.connect();
			graphite.send(name, String.format("%12.3f", value), System.currentTimeMillis());
			graphite.flush();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
	}
	public void start()
	{
		try {
			inetSocketAddress = new InetSocketAddress(graphiteHost, graphitePort);
			graphite = new Graphite(inetSocketAddress);
			graphite.connect();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
	}
	public void stop()
	{
		if(graphite.isConnected())
			try {
				graphite.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
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
	}
	@Override
	public void setConfig(Map config) {
		// TODO Auto-generated method stub
		this.config = config;
		this.processConfig();
	}
}
