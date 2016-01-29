package storm.metrics;

import java.util.regex.Pattern;


public class MetricFilter {
	
	private static final String EMIT_COUNT = "emit-count";
	private static final String LATENCY = "latency";
	private static final String EXECUTE = "execute-count";
	private static final String COUNT = "count";
	private static final String FAIL_COUNT = "fail-count";
	private static final String ACK_COUNT = "ack-count";
	private static final String TRANSFER_COUNT = "transfer-count";
	
	public static Boolean isFilter( String name)
	{
		//String str = EMIT + " | " + LATENCY + " | " + EXECUTE;
		//return name.matches(str);
		/*if(name.indexOf(EMIT_COUNT) > 0 || name.indexOf(LATENCY) > 0 ||
				name.indexOf(EXECUTE) > 0 ||
				name.indexOf(FAIL_COUNT) > 0 ||
				name.indexOf(ACK_COUNT) > 0 ||
				name.indexOf(TRANSFER_COUNT) > 0)
			return true;
		else
			return false;*/
		Boolean filter = false;
		if(name.endsWith("metrics") || name.endsWith("_system"))
			filter = true;
		else if(name.endsWith("capacity"))
			filter = true;
		return filter;
	}
}
