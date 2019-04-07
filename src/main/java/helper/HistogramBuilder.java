package helper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HistogramBuilder {

	private final int BIN_QUANTITY = 7;
	private double min = 0;
	private double max;
	private List<Double> times = new ArrayList<>();

	public HistogramBuilder(double limitTime) {
		this.max = limitTime;
	}

	public void addCollision(double time) {
		this.times.add(time);
	}

	public Histogram<Range, Integer> buildHistogram() {
		Collections.sort(times);

		double step = (max - min) / BIN_QUANTITY;

		List<Range> ranges = IntStream
				.range(0, BIN_QUANTITY).boxed()
				.map(r -> new Range(min + r * step, max + (r + 1) * step))
				.collect(Collectors.toList());

		Map<Range, Integer> histogram = new HashMap<>();
		for (Range range : ranges) {
			int timesPerBin = 0;

			for (Double time : times) {
				if (range.isIn(time)) {
					timesPerBin += 1;
				}
			}

			histogram.put(range, timesPerBin);
		}

		return new Histogram<>(histogram);
	}
}
