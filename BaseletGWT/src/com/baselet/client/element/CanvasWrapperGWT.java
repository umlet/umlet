package com.baselet.client.element;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

public class CanvasWrapperGWT extends CanvasWrapper {

	private Canvas canvas;
	
	public CanvasWrapperGWT() {
		canvas = Canvas.createIfSupported();
	}

	@Override
	public void redrawIfNecessary() {
		if (getRedrawNecessary()) {
			setRedrawNecessary(false);
			Context2d geCtx = canvas.getContext2d();
			geCtx.clearRect(-1000000, -1000000, 2000000, 2000000);
			geCtx.setFillStyle(getGridElement().getColor());
			geCtx.fillRect(0, 0, getGridElement().getBounds().getWidth(), getGridElement().getBounds().getHeight());
			geCtx.fill();
		}
	}
	
	public void drawOn(Context2d context) {
		context.drawImage(canvas.getCanvasElement(), getGridElement().getBounds().getX(), getGridElement().getBounds().getY());
	}
}
