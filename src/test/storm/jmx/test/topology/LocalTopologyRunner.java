package storm.jmx.test.topology;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class LocalTopologyRunner {

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException{
		// TODO Auto-generated method stub
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("name-spout", new ExampleSpout(),2);
		builder.setBolt("name-bolt", new ExampleBolt(),2).shuffleGrouping("name-spout");
		
		Config config = new Config();
				
		StormSubmitter.submitTopology("Storm-Metrics-Example", config, 
					builder.createTopology());
		
	}

}
