package storm.reporter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.metrics.AbstractMetricReporter;

public class UDPMetricReporter extends AbstractMetricReporter {
	public static final Logger LOG = LoggerFactory.getLogger(UDPMetricReporter.class);
	private String ipAddress;
	private int port;
	
	private InetAddress address;
	DatagramSocket socket;
	@Override
	protected void processConfig() {
		// TODO Auto-generated method stub
		ipAddress = config.containsKey(UDP_IP_ADDRESS)?
				config.get(UDP_IP_ADDRESS).toString() :
				"localhost";
		port = config.containsKey(UDP_PORT) ?
				Integer.parseInt(config.get(UDP_PORT).toString()) :
					14445;
	}

	@Override
	public void setConfig(Map config) {
		// TODO Auto-generated method stub
		this.config = config;
		this.processConfig();
	}

	@Override
	public void sendMetrics(String name, Double value) {
		// TODO Auto-generated method stub
		String str = "Name: " + name + " Value:" + String.format("%-12.2f", value);
		int length = str.getBytes().length;
		DatagramPacket packet = new DatagramPacket(str.getBytes(), length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
	}

	@Override
	public void start(){
		// TODO Auto-generated method stub
		try {
			address = InetAddress.getByName(ipAddress);
			socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
	}

	@Override
	public void stop(){
		// TODO Auto-generated method stub
		socket.close();
	}

}
