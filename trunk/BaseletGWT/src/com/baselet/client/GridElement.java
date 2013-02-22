package com.baselet.client;

public class GridElement {

	Rectangle canvasRectangle;
	
	public GridElement(Rectangle bounds) {
		this.canvasRectangle = bounds;
	}
	
	public Rectangle getBounds() {
		return canvasRectangle;
	}
	
	public boolean contains(int x, int y) {
		return canvasRectangle.contains(x, y);
	}
}
