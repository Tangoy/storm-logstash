package storm.jmx.test.topology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class ExampleSpout extends BaseRichSpout{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private SpoutOutputCollector collector;
	private static final Map<Integer, String> map = new HashMap<Integer, String>();
	static {
		map.put(0, "Name0");
		map.put(1, "Name1");
		map.put(2, "Name2");
		map.put(3, "Name3");
		map.put(4, "Name4");
		map.put(5, "Name5");
		map.put(6, "Name6");
		map.put(7, "Name7");
		map.put(8, "Name8");
		map.put(9, "Name9");
		map.put(10, "Name10");
	}
	
	public void nextTuple() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int num = rand.nextInt(10);
		collector.emit(new Values(map.get(num)));
		
	}

	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
		
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("name"));
	}
	
	@Override
	public void close()
	{

	}
}
