package helper;

import org.apache.commons.math3.util.Precision;

public class Range implements Comparable<Range> {

	private double min;
	private double max;
	private final double EPSILON = Math.pow(10, -8);

	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public boolean isIn(double value) {
		return Precision.compareTo(value, min, EPSILON) >= 0 && value < max;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Range range = (Range) o;

		if (Precision.compareTo(range.min, min, EPSILON) != 0) return false;
		return Precision.compareTo(range.max, max, EPSILON) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(min);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(max);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return String.format("[%.3f,%.3f)", min, max);
	}

	@Override
	public int compareTo(Range o) {
		return Double.compare(min, o.getMin());
	}
}