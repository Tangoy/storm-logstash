package storm.jmx.test.topology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordSpout implements IRichSpout {
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
		map.put(11, "Name11");
		map.put(12, "Name12");
	}
	SpoutOutputCollector colector;
	@Override
	public void ack(Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		colector.emit(new Values(map.get(1)));
	}

	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector colector) {
		// TODO Auto-generated method stub
		this.colector = colector;
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("name"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
