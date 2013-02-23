package com.baselet.client;

import com.google.gwt.canvas.dom.client.CssColor;

public class GridElement {

	private Rectangle canvasRectangle;
	
	private CssColor color;
	
	private CanvasWrapper canvasUnderlying;
	
	public GridElement(Rectangle canvasRectangle, CssColor color, CanvasWrapper canvasUnderlying) {
		super();
		this.canvasRectangle = canvasRectangle;
		this.color = color;
		this.canvasUnderlying = canvasUnderlying;
		this.canvasUnderlying.setGridElement(this);
	}

	public Rectangle getBounds() {
		return canvasRectangle;
	}
	
	public boolean contains(int x, int y) {
		return canvasRectangle.contains(x, y);
	}
	
	public CssColor getColor() {
		return color;
	}
	
	public void move(int diffX, int diffY) {
		canvasRectangle.move(diffX, diffY);
		canvasUnderlying.setRedrawNecessary(true);
	}

	public void updateCanvas() {
		canvasUnderlying.redrawIfNecessary();
	}
	
	public CanvasWrapper getCanvasUnderlying() {
		return canvasUnderlying;
	}
}
