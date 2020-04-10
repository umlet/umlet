package com.baselet.element.sticking;

public class PointChange {
	private final Integer index;
	private final int diffX;
	private final int diffY;

	public PointChange(Integer index, int diffX, int diffY) {
		super();
		this.index = index;
		this.diffX = diffX;
		this.diffY = diffY;
	}

	public Integer getIndex() {
		return index;
	}

	public int getDiffX() {
		return diffX;
	}

	public int getDiffY() {
		return diffY;
	}

	@Override
	public String toString() {
		return "PointChange [index=" + index + ", diffX=" + diffX + ", diffY=" + diffY + "]";
	}

}
