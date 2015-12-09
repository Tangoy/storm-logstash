package storm.jmx.topology;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class ExampleBolt extends BaseRichBolt {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		String name = tuple.getStringByField("name");
		System.out.println(name);
	}

	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub
		

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}
}
