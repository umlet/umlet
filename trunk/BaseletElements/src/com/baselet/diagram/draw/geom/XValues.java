package com.baselet.diagram.draw.geom;

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
		return right-left;
	}
	
	public void addLeft(float inc) {
		left+=inc;
	}
	
	public void subRight(float inc) {
		right-=inc;
	}
	
	/**
	 * returns the intersection of both points [eg: (2,5) intersect (1,4) = (2,4)]
	 */
	public XValues intersect(XValues other) {
		double maxLeft = left;
		double minRight = right;
		if (!other.left.equals(Double.NaN) && other.left > this.left) maxLeft = other.left;
		if (!other.right.equals(Double.NaN) && other.right < this.right) minRight = other.right;
		return new XValues(maxLeft, minRight);
	}
	
}
