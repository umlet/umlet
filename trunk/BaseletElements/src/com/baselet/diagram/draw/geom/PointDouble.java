package com.baselet.diagram.draw.geom;


public class PointDouble {

	public Double x;
	public Double y;
	
	public PointDouble() {
	}
	
	public PointDouble(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public Double getY() {
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

	public boolean equalsContent(PointDouble other) {
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "(x=" + x + " y=" + y + ")";
	}
	
}
