package storm.jmx.reporter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ganglia.GangliaReporter;

import info.ganglia.gmetric4j.gmetric.GMetric;
import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;
import storm.jmx.metrics.MetricReporter;

public class GangliaMetricReporter extends MetricReporter{
	public void start() throws IOException
	{
		final GMetric ganglia = new GMetric("localhost", 8649, UDPAddressingMode.MULTICAST,1);
		final GangliaReporter reporter = GangliaReporter.forRegistry(METRIC_REGISTRY)
											.convertDurationsTo(TimeUnit.SECONDS)
											.convertRatesTo(TimeUnit.MILLISECONDS)
											.withDMax(1000)
											.withTMax(1000)
											.build(ganglia);
		reporter.start(2, TimeUnit.SECONDS);

	}
}
