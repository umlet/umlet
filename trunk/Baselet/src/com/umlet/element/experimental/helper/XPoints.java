package com.umlet.element.experimental.helper;

public class XPoints {

	private float left;
	private float right;
	
	public XPoints(float left, float right) {
		super();
		this.left = left;
		this.right = right;
	}
	
	public float distance() {
		return right-left;
	}

	public float getLeft() {
		return left;
	}

	public float getRight() {
		return right;
	}
	
	public void addLeft(float inc) {
		left+=inc;
	}
	
	public void subRight(float inc) {
		right-=inc;
	}
	
}
