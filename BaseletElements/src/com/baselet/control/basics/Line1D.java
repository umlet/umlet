package com.baselet.control.basics;

public class Line1D {

	private final Double low;
	private final Double high;

	public Line1D(double low, double high) {
		super();
		this.low = low;
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public double getHigh() {
		return high;
	}

	public double getSpace() {
		return high - low;
	}

	public boolean contains(double value) {
		return low <= value && value <= high;
	}

	/**
	 * returns the intersection of both points [eg: (2,5) intersect (1,4) = (2,4)]
	 * @param nanPriority if true then NaN has priority over other values, otherwise other values have priority
	 */
	public Line1D intersect(Line1D other, boolean nanPriority) {
		Double maxLeft = low;
		Double minRight = high;
		if (nanPriority) {
			if (other.low.equals(Double.NaN) || other.low > low) {
				maxLeft = other.low;
			}
			if (other.high.equals(Double.NaN) || other.high < high) {
				minRight = other.high;
			}
		}
		else {
			if (low.equals(Double.NaN) || other.low > low) {
				maxLeft = other.low;
			}
			if (high.equals(Double.NaN) || other.high < high) {
				minRight = other.high;
			}
		}
		return new Line1D(maxLeft, minRight);
	}

	@Override
	public String toString() {
		return "Line1D [low=" + low + ", high=" + high + "]";
	}

}
