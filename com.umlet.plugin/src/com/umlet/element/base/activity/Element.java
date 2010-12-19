package com.umlet.element.base.activity;

import java.awt.*;

public abstract class Element {

	private Graphics2D graphics;
	private int leftwidth, rightwidth, height;
	private int padding;
	private Coordinate cord;
	private String id;
	private Row row;
	
	//specifies whether the element must not connect to another element.
	private boolean terminated;
	
	public Element(Graphics2D g, int padding, String id) {
		this.graphics = g;
		this.padding = padding;
		this.id = id;
		this.leftwidth=0;
		this.rightwidth=0;
		this.height=0;
		this.cord = new Coordinate(0,0);
	}
	
	public void setRow(Row row) {
		this.row = row;
	}
	
	public Row getRow() {
		return this.row;
	}
	
	public final void setTerminated() {
		this.terminated = true;
	}
	
	public final void setNotTerminated() {
		this.terminated = false;
	}
	
	public Coordinate getPosition() {
		return this.cord;
	}
	
	public void setY(int y) {
		this.cord.y = y;
	}
	
	public void setX(int x) {
		this.cord.x = x;
	}
	
	protected Graphics2D getGraphics() {
		return graphics;
	}
	
	public String getId() {
		return this.id;
	}
	
	protected int getHeight() {
		return height;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

	protected int getLeftWidth() {
		return leftwidth;
	}
	
	protected int getRightWidth() {
		return rightwidth;
	}
	
	protected int getWidth() {
		return this.getLeftWidth() + this.getRightWidth();
	}

	protected void setWidth(int width) {
		this.setLeftWidth(width/2);
		this.setRightWidth(width - width/2);
	}
	
	protected void setLeftWidth(int width) {
		this.leftwidth = width;
	}
	
	protected void setRightWidth(int width) {
		this.rightwidth = width;
	}

	protected int getPadding() {
		return this.padding;
	}

	public abstract void paint();
	
	public boolean connectIn() {
		return true;
	}
	
	public boolean connectOut_overrideable() {
		return true;
	}
	
	public final boolean connectOut() {
		return this.connectOut_overrideable() && !this.terminated;
	}
	
	public boolean arrowOut() {
		return true;
	}
	
	public boolean arrowIn() {
		return true;
	}
	
	public void printData(String prefix) {
		System.out.println(prefix + this.getClass().getSimpleName());
	}
	
	//get connect for GOTO element
	protected Coordinate getNonStdConnectIn(Direction dir) {
		return this.getConnect(dir);
	}
	
	protected Coordinate getNonStdConnectOut(Direction dir) {
		return this.getConnect(dir);
	}
	
	protected Coordinate getConnect(Direction dir) {
		Coordinate c = this.cord.clone();
		if(dir == Direction.TOP)
			c.y -= this.getHeight()/2;
		else if(dir == Direction.BOTTOM)
			c.y += this.getHeight() - this.getHeight()/2;
		else if(dir == Direction.LEFT)
			c.x -= this.getLeftWidth();
		else if(dir == Direction.RIGHT)
			c.x += this.getRightWidth();
		return c;
	}
}
