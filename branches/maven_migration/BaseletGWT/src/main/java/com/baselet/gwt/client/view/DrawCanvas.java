package com.baselet.gwt.client.view;

import java.util.List;

import com.baselet.control.SharedConstants;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.gwt.client.element.ComponentGwt;
import com.baselet.gwt.client.element.ElementFactory;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.FocusWidget;
import com.umlet.element.experimental.ElementId;

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

	public CanvasElement getCanvasElement() {
		return canvas.getCanvasElement();
	}

	public String toDataUrl(String type) {
		return canvas.toDataUrl(type);
	}

	void draw(boolean drawEmptyInfo, List<GridElement> gridElements, Selector selector) {
		if (SharedConstants.isDevMode) {
			CanvasUtils.drawGridOn(getContext2d());
		}

		if (drawEmptyInfo && gridElements.isEmpty()) {
			drawEmptyInfoText();
		} else {
			//		if (tryOptimizedDrawing()) return;
			for (GridElement ge : gridElements) {
				((ComponentGwt) ge.getComponent()).drawOn(canvas.getContext2d(), selector.isSelected(ge));
			}
		}
	}

	private void drawEmptyInfoText() {
		double elWidth = 440;
		double elHeight = 150;
		double elXPos = getWidth()/2 - elWidth/2;
		double elYPos = getHeight()/2 - elHeight;
		GridElement emptyElement = ElementFactory.create(ElementId.Text, new Rectangle(elXPos, elYPos, elWidth, elHeight), "halign=center\nDouble-click on an element to add it to the diagram (or use drag&drop)\n\n<Import> uxf Files using the Menu or simply drag them into the diagram\n<Export> diagrams to Standalone-UMLet-compatible uxf or png \n<Save> diagrams to persistent browser storage\n\nOnly Improved Elements work in standalone and web umlet\nWeb-Umlet Relations currently don't work in Standalone and vica versa\n\nPlease report bugs at http://code.google.com/p/umlet/", "", null);
		((ComponentGwt) emptyElement.getComponent()).drawOn(canvas.getContext2d(), false);

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
