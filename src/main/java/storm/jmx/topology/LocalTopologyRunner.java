package storm.jmx.topology;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class LocalTopologyRunner {

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException{
		// TODO Auto-generated method stub
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("name-spout", new ExampleSpout(),1);
		builder.setBolt("name-bolt", new ExampleBolt(),2).shuffleGrouping("name-spout");
		
		Config config = new Config();
		//config.setDebug(true);
		/*config.put("storm.reporter", "storm.jmx.reporter.GangliaMetricReporter");
		config.put("storm.ganglia.host", "localhost");
		config.put("storm.ganglia.port", 8649);
		
		config.put("storm.reporter", "storm.jmx.reporter.JmxMetricReporter");
		config.put("domainname", "stom.metrics");*/
		StormSubmitter.submitTopology("Storm-Metrics-Example", config, 
					builder.createTopology());
		//Utils.sleep(10000);
		
	}

}
