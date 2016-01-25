package storm.jmx.test.topology;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class ClusterTopologyRunner {
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException
	{
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("wordspout", new WordSpout(), 1)
			.setNumTasks(2);
		builder.setBolt("wordbolt", new WordBolt(), 1).shuffleGrouping("wordspout")
			.setNumTasks(1);
		Config config = new Config();
		//config.setNumWorkers(2);
		config.setMessageTimeoutSecs(60);
		StormSubmitter.submitTopology("Cluster-Topology-Tester", 
				config, builder.createTopology());
	}
}
