package com.baselet.client;


public abstract class CanvasWrapper {

	private Boolean redrawNecessary = true;
	private GridElement gridElement;
	
	protected Boolean getRedrawNecessary() {
		return redrawNecessary;
	}
	
	protected void setRedrawNecessary(Boolean redraw) {
		this.redrawNecessary = redraw;
	}

	public abstract void redrawIfNecessary();

	public void setGridElement(GridElement gridElement) {
		this.gridElement = gridElement;
	}
	
	public GridElement getGridElement() {
		return gridElement;
	}

}
