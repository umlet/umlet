package com.baselet.client;

import com.google.gwt.canvas.dom.client.CssColor;

public class GridElement {

	Rectangle canvasRectangle;
	
	CssColor color;
	
	public GridElement(Rectangle canvasRectangle, CssColor color) {
		super();
		this.canvasRectangle = canvasRectangle;
		this.color = color;
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
}
