package storm.metrics;

import com.codahale.metrics.Gauge;

public class GaugeMetric<T> implements Gauge<T> {
	private T value;
	public void setValue(T value)
	{
		this.value = value;
	}
	public T getValue() {
		// TODO Auto-generated method stub
		return value;
	}
}
