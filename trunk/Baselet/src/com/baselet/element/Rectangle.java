package com.baselet.element;

public class Rectangle {

	public int x;
	public int y;
	public int width;
	public int height;

	public Rectangle() {
		super();
	}

	public Rectangle(int x, int y, int width, int height) {
		this();
		setBounds(x, y, width, height);
	}

	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getX2() {
		return x + width;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public int getY2() {
		return y + height;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void move(int diffX, int diffY) {
		this.x += diffX;
		this.y += diffY;
	}

	public boolean contains(Point p) {
		return contains(new Rectangle(p.getX(), p.getY(), 0, 0));
	}

	public boolean contains(Rectangle other) {
		return getX() <= other.getX() && getX2() >= other.getX2() && getY() <= other.getY() && getY2() >= other.getY2();
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public boolean intersects(Rectangle other) {
		if (getY2() < other.getY()) return false;
		if (getY() > other.getY2()) return false;
		if (getX2() < other.getX()) return false;
		if (getX() > other.getX2()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Rectangle other = (Rectangle) obj;
		if (height != other.height) return false;
		if (width != other.width) return false;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rectangle [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

}
