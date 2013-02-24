package com.baselet.client.element;

import com.baselet.client.Rectangle;
import com.google.gwt.canvas.dom.client.CssColor;

public class GridElement {

	private Rectangle canvasRectangle;
	
	private CssColor color;
	
	private CanvasWrapper canvasWrapper;
	
	public GridElement(Rectangle canvasRectangle, CssColor color, CanvasWrapper canvasWrapper) {
		super();
		this.canvasRectangle = canvasRectangle;
		this.color = color;
		this.canvasWrapper = canvasWrapper;
		this.canvasWrapper.setGridElement(this);
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
		canvasWrapper.setRedrawNecessary(true);
	}

	public void updateCanvas() {
		canvasWrapper.redrawIfNecessary();
	}
	
	public CanvasWrapper getCanvasUnderlying() {
		return canvasWrapper;
	}
}
