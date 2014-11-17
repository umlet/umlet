package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
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
		graphics = g;
		this.padding = padding;
		this.id = id;
		leftwidth = 0;
		rightwidth = 0;
		height = 0;
		cord = new Point(0, 0);
		zoom = handler.getZoomFactor();
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public Row getRow() {
		return row;
	}

	public final void setTerminated() {
		terminated = true;
	}

	public final void setNotTerminated() {
		terminated = false;
	}

	public Point getPosition() {
		return cord;
	}

	public void setY(int y) {
		cord.y = y;
	}

	public void setX(int x) {
		cord.x = x;
	}

	protected Graphics2D getGraphics() {
		return graphics;
	}

	public String getId() {
		return id;
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
		return getLeftWidth() + getRightWidth();
	}

	protected void setWidth(int width) {
		setLeftWidth(width / 2);
		setRightWidth(width - width / 2);
	}

	protected void setLeftWidth(int width) {
		leftwidth = width;
	}

	protected void setRightWidth(int width) {
		rightwidth = width;
	}

	protected int getPadding() {
		return padding;
	}

	public abstract void paint();

	public boolean connectIn() {
		return true;
	}

	public boolean connectOut_overrideable() {
		return true;
	}

	public final boolean connectOut() {
		return connectOut_overrideable() && !terminated;
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
		return getConnect(dir);
	}

	protected Point getNonStdConnectOut(Direction dir) {
		return getConnect(dir);
	}

	protected Point getConnect(Direction dir) {
		Point c = (Point) cord.clone();
		if (dir == Direction.UP) {
			c.y -= getHeight() / 2;
		}
		else if (dir == Direction.DOWN) {
			c.y += getHeight() - getHeight() / 2;
		}
		else if (dir == Direction.LEFT) {
			c.x -= getLeftWidth();
		}
		else if (dir == Direction.RIGHT) {
			c.x += getRightWidth();
		}
		return c;
	}

	protected DiagramHandler getHandler() {
		return handler;
	}

	protected float getZoom() {
		return zoom;
	}
}
