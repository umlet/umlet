package com.umlet.element.experimental.helper;

public class LineHandlerCount implements LineHandler {

	private float sum = 0;
	
	@Override
	public boolean countOnly() {
		return true;
	}

	@Override
	public void addToYPos(float inc) {
		sum += inc;
	}

	public float getSum() {
		return sum;
	}
	
}
