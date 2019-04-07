package helper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Histogram<X, Y extends Number> {

	private Map<X, Y> values;
	private double sumOfValues;
	private double binWidth;

	public Histogram(Map<X, Y> values) {
		this.values = values;
		sumOfValues = values.values().stream().mapToDouble(Number::doubleValue).sum();
	}

	public Histogram(Map<X, Y> values, double binWidth) {
		this(values);
		this.binWidth = binWidth;
	}

	public List<X> rangeList() {
		return values.keySet().stream().sorted().collect(Collectors.toList());
	}

	public Y getValue(X range) {
		return values.get(range);
	}

	public double getProbability(X range) {
		return getValue(range).doubleValue() / sumOfValues;
	}

	public double getProbabilityDistributed(X range) {
		return getValue(range).doubleValue() / (sumOfValues * binWidth);
	}

}
