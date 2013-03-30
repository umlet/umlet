package com.baselet.gwt.client;

public class Rectangle {

	private int x;
	private int y;
	private int width;
	private int height;
	
	public Rectangle(int x, int y, int width, int height) {
		super();
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

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void move(int diffX, int diffY) {
		this.x += diffX;
		this.y += diffY;
	}

	public boolean contains(int inX, int inY) {
		return getX() <= inX && inX <= getX2() && getY() <= inY && inY <= getY2();
	}
	
}
