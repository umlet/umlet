package com.baselet.diagram.draw.geom;

public class DimensionDouble {

	private float height;
	private float width;

	public DimensionDouble(float width, float height) {
		super();
		this.width = width;
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "Dimension [height=" + height + ", width=" + width + "]";
	}
	
}
