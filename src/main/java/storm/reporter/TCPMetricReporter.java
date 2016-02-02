package storm.reporter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.metrics.AbstractMetricReporter;

public class TCPMetricReporter extends AbstractMetricReporter {
	public static final Logger LOG = LoggerFactory.getLogger(TCPMetricReporter.class);
	
	private String ipAddress;
	private int port;
	
	private Socket socket;
	@Override
	protected void processConfig() {
		// TODO Auto-generated method stub
		ipAddress = config.containsKey(TCP_IP_ADDRESS)?
				config.get(TCP_IP_ADDRESS).toString() :
				"localhost";
		port = config.containsKey(TCP_PORT) ?
				Integer.parseInt(config.get(TCP_PORT).toString()) :
					14445;
	}

	@Override
	public void setConfig(Map config) {
		// TODO Auto-generated method stub
		this.config = config;
		this.processConfig();
	}

	@Override
	public void sendMetrics(String name, Double value){
		// TODO Auto-generated method stub
		PrintWriter out;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("Name: " + name + " Value: " + String.format("%-12.3f", value));
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
	}

	@Override
	public void start(){
		// TODO Auto-generated method stub
		try {
			socket = new Socket(InetAddress.getByName(ipAddress), port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
	}

	@Override
	public void stop(){
		// TODO Auto-generated method stub
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
	}

}
