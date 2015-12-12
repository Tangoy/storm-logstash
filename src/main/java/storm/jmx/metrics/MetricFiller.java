package storm.jmx.metrics;

public class MetricFiller {
	
	private static final String COUNT = "count";
	private static final String LATENCY = "latency";
	private static final String EXECUTE = "execute";
	public static Boolean isFiller(String name)
	{
		if((name.indexOf(COUNT)>0) || (name.indexOf(LATENCY) > 0)
				|| (name.indexOf(EXECUTE) > 0))
			return true;
		else 
			return false;
	}
}
