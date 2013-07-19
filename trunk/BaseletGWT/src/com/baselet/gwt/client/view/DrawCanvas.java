package com.baselet.gwt.client.view;

import java.util.List;

import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.gwt.client.element.GwtComponent;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.FocusWidget;

public class DrawCanvas {
	private Canvas canvas = Canvas.createIfSupported();
	
	public FocusWidget getWidget() {
		return canvas;
	}
	
	public Context2d getContext2d() {
		return canvas.getContext2d();
	}
	
	public void clearAndSetSize(int width, int height) {
		// setCoordinateSpace always clears the canvas. To avoid that see https://groups.google.com/d/msg/google-web-toolkit/dpc84mHeKkA/3EKxrlyFCEAJ
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
	}
	
	public int getWidth() {
		return canvas.getCoordinateSpaceWidth();
	}
	
	public int getHeight() {
		return canvas.getCoordinateSpaceHeight();
	}

	public String toPng() {
		return canvas.toDataUrl("image/png");
	}

	public CanvasElement getCanvasElement() {
		return canvas.getCanvasElement();
	}
	
	public void drawElements(List<GridElement> gridElements, Selector selector) {
		//		if (tryOptimizedDrawing()) return;
		for (GridElement ge : gridElements) {
			((GwtComponent) ge.getComponent()).drawOn(canvas.getContext2d(), selector.isSelected(ge));
		}
	}

	//TODO would not work because canvas gets always resized and therefore cleaned -> so everything must be redrawn
	//	private boolean tryOptimizedDrawing() {
	//		List<GridElement> geToRedraw = new ArrayList<GridElement>();
	//		for (GridElement ge : gridElements) {
	//			if(((GwtComponent) ge.getComponent()).isRedrawNecessary()) {
	//				for (GridElement geRedraw : geToRedraw) {
	//					if (geRedraw.getRectangle().intersects(ge.getRectangle())) {
	//						return false;
	//					}
	//				}
	//				geToRedraw.add(ge);
	//			}
	//		}
	//
	//		for (GridElement ge : gridElements) {
	//			elementCanvas.getContext2d().clearRect(0, 0, ge.getRectangle().getWidth(), ge.getRectangle().getHeight());
	//			((GwtComponent) ge.getComponent()).drawOn(elementCanvas.getContext2d());
	//		}
	//		return true;
	//	}
}
