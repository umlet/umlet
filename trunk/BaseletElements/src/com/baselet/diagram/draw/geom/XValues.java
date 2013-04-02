package com.baselet.diagram.draw.geom;

public class XValues {

	private Float left;
	private Float right;
	
	public XValues(float left, float right) {
		super();
		this.left = left;
		this.right = right;
	}

	public Float getLeft() {
		return left;
	}

	public Float getRight() {
		return right;
	}
	
	public Float getSpace() {
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
		float maxLeft = left;
		float minRight = right;
		if (!other.left.equals(Float.NaN) && other.left > this.left) maxLeft = other.left;
		if (!other.right.equals(Float.NaN) && other.right < this.right) minRight = other.right;
		return new XValues(maxLeft, minRight);
	}
	
}
