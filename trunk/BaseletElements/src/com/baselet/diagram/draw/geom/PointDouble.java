package com.baselet.diagram.draw.geom;


public class PointDouble {

	public double x;
	public double y;
	
	public PointDouble() {
	}
	
	public PointDouble(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

	public void move(double diffX, double diffY) {
		this.x += diffX;
		this.y += diffY;
	}

    public double distance(PointDouble o) {
	double distX = o.getX() - this.getX();
	double distY = o.getY() - this.getY();
	return Math.sqrt(distX * distX + distY * distY);
    }
	public PointDouble copy() {
		return new PointDouble(x, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PointDouble other = (PointDouble) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "(x=" + x + " y=" + y + ")";
	}
	
}
