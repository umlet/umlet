package com.baselet.control.basics;

public class XValues {

	private Double left;
	private Double right;

	public XValues(double left, double right) {
		super();
		this.left = left;
		this.right = right;
	}

	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public double getSpace() {
		return right - left;
	}

	public void addLeft(double inc) {
		left += inc;
	}

	public void subRight(double inc) {
		right -= inc;
	}

	public static XValues createForEllipse(double y, double height, double width) {
		double halfHeight = height / 2.0;
		double halfWidth = width / 2.0;
		double halfEllipseWidthOnY = Math.sqrt((1 - Math.pow(halfHeight - y, 2) / Math.pow(halfHeight, 2)) * Math.pow(halfWidth, 2));
		XValues returnVal = new XValues(halfWidth - halfEllipseWidthOnY, halfWidth + halfEllipseWidthOnY);
		return returnVal;

	}

	/**
	 * returns the intersection of both points [eg: (2,5) intersect (1,4) = (2,4)]
	 * @param nanPriority if true then NaN has priority over other values, otherwise other values have priority
	 */
	public XValues intersect(XValues other, boolean nanPriority) {
		Double maxLeft = left;
		Double minRight = right;
		if (nanPriority) {
			if (other.left.equals(Double.NaN) || other.left > left) {
				maxLeft = other.left;
			}
			if (other.right.equals(Double.NaN) || other.right < right) {
				minRight = other.right;
			}
		}
		else {
			if (left.equals(Double.NaN) || other.left > left) {
				maxLeft = other.left;
			}
			if (right.equals(Double.NaN) || other.right < right) {
				minRight = other.right;
			}
		}
		return new XValues(maxLeft, minRight);
	}

	@Override
	public String toString() {
		return "XValues [left=" + left + ", right=" + right + "]";
	}

}
