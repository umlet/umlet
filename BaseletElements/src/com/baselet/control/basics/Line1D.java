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

	/**
	 * @return <code>high - low</code>
	 */
	public double getSpace() {
		return high - low;
	}

	/**
	 * @return <code>(high + low) / 2</code>
	 */
	public double getCenter() {
		return (high + low) / 2;
	}

	public boolean contains(double value) {
		return low <= value && value <= high;
	}

	public boolean isIntersecting(Line1D other) {
		// first 2 return true if there is a partial overlap or this contains other
		// 3rd returns true if other contains this
		// therefore all 4 possibilities for an overlap are covered
		boolean isIntersecting = contains(other.getLow()) || contains(other.getHigh()) || other.contains(getLow());
		return isIntersecting;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (high == null ? 0 : high.hashCode());
		result = prime * result + (low == null ? 0 : low.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Line1D other = (Line1D) obj;
		if (high == null) {
			if (other.high != null) {
				return false;
			}
		}
		else if (!high.equals(other.high)) {
			return false;
		}
		if (low == null) {
			if (other.low != null) {
				return false;
			}
		}
		else if (!low.equals(other.low)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Line1D [low=" + low + ", high=" + high + "]";
	}

}
