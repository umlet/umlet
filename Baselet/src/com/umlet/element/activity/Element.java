package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public abstract class Element {

	private Graphics2D graphics;
	private int leftwidth, rightwidth, height;
	private int padding;
	private Point cord;
	private String id;
	private Row row;
	private DiagramHandler handler;
	private float zoom;

	// specifies whether the element must not connect to another element.
	private boolean terminated;

	public Element(DiagramHandler handler, Graphics2D g, int padding, String id) {
		this.handler = handler;
		this.graphics = g;
		this.padding = padding;
		this.id = id;
		this.leftwidth = 0;
		this.rightwidth = 0;
		this.height = 0;
		this.cord = new Point(0, 0);
		this.zoom = handler.getZoomFactor();
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

	public Point getPosition() {
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
		this.setLeftWidth(width / 2);
		this.setRightWidth(width - width / 2);
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

	// get connect for GOTO element
	protected Point getNonStdConnectIn(Direction dir) {
		return this.getConnect(dir);
	}

	protected Point getNonStdConnectOut(Direction dir) {
		return this.getConnect(dir);
	}

	protected Point getConnect(Direction dir) {
		Point c = (Point) this.cord.clone();
		if (dir == Direction.TOP) c.y -= this.getHeight() / 2;
		else if (dir == Direction.BOTTOM) c.y += this.getHeight() - this.getHeight() / 2;
		else if (dir == Direction.LEFT) c.x -= this.getLeftWidth();
		else if (dir == Direction.RIGHT) c.x += this.getRightWidth();
		return c;
	}

	protected DiagramHandler getHandler() {
		return handler;
	}

	protected float getZoom() {
		return zoom;
	}
}
