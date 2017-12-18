package com.baselet.control.basics.geom;

/**
 * should be removed after every Dimension has been replaced by DimensionFloat
 */
public class Dimension {
	public int width;
	public int height;

	public Dimension() {}

	public Dimension(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
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
		Dimension other = (Dimension) obj;
		if (height != other.height) {
			return false;
		}
		if (width != other.width) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Dimension [width=" + width + ", height=" + height + "]";
	}

}
