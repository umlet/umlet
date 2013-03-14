package com.baselet.element;


public class Point {

	public int x;
	public int y;
	
	public Point() {
	}
	
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public void move(int diffX, int diffY) {
		this.x += diffX;
		this.y += diffY;
	}

    public double distance(Point o) {
	double distX = o.getX() - this.getX();
	double distY = o.getY() - this.getY();
	return Math.sqrt(distX * distX + distY * distY);
    }
	
}
