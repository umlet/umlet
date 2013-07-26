package com.baselet.gwt.client.view;

import java.util.List;

import com.baselet.control.NewGridElementConstants;
import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.gwt.client.element.ElementFactory;
import com.baselet.gwt.client.element.GwtComponent;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.FocusWidget;
import com.umlet.element.experimental.ElementId;

public class DrawCanvas {
	private Canvas canvas = Canvas.createIfSupported();

	private int minWidth = 0;
	private int minHeight = 0;

	private AutoResizeScrollDropPanel scrollPanel;

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
		if (NewGridElementConstants.isDevMode) {
			CanvasUtils.drawGridOn(getContext2d());
		}

		if (drawEmptyInfo && gridElements.isEmpty()) {
			drawEmptyInfoText();
		} else {
			//		if (tryOptimizedDrawing()) return;
			for (GridElement ge : gridElements) {
				((GwtComponent) ge.getComponent()).drawOn(canvas.getContext2d(), selector.isSelected(ge));
			}
		}
	}

	public void recalcSizeForGridElementsAndDraw(boolean drawEmptyInfo, List<GridElement> gridElements, Selector selector) {
		if (scrollPanel == null) return;

		Rectangle diagramRect = SharedUtils.getGridElementsRectangle(gridElements);
		Rectangle visibleRect = scrollPanel.getVisibleBounds();
		// realign top left corner of the diagram back to the canvas and remove invisible whitespace outside of the diagram
		final int xTranslate = Math.min(visibleRect.getX(), diagramRect.getX()); // can be positive (to cut upper left whitespace without diagram) or negative (to move diagram back to the visible canvas which starts at (0,0))
		final int yTranslate = Math.min(visibleRect.getY(), diagramRect.getY());
		scrollPanel.moveHorizontalScrollbar(-xTranslate);
		scrollPanel.moveVerticalScrollbar(-yTranslate);

		if (xTranslate < 0 || yTranslate < 0) {
			for (GridElement ge : gridElements) {
				ge.setLocationDifference(-xTranslate, -yTranslate);
			}
		}

		diagramRect = SharedUtils.getGridElementsRectangle(gridElements); // refresh after possible changed locations
		int width = Math.max(minWidth, Math.max(getWidth(), diagramRect.getX2())-xTranslate);
		int height = Math.max(minHeight, Math.max(getHeight(), diagramRect.getY2())-yTranslate);

		// crop empty space on the lower right
		int xCropLimit = Math.max(diagramRect.getX2(), visibleRect.getX2());
		int yCropLimit = Math.max(diagramRect.getY2(), visibleRect.getY2());
		final int rightInvisiblePart = canvas.getCoordinateSpaceWidth() - xCropLimit;
		final int lowerInvisiblePart = canvas.getCoordinateSpaceHeight() - yCropLimit;
		width -= rightInvisiblePart;
		height -= lowerInvisiblePart;

		clearAndSetSize(width, height);



		draw(drawEmptyInfo, gridElements, selector);
	}

	private void drawEmptyInfoText() {
		double elWidth = 440;
		double elHeight = 80;
		double elXPos = getWidth()/2 - elWidth/2;
		double elYPos = getHeight()/2 - elHeight;
		GridElement emptyElement = ElementFactory.create(ElementId.Text, new Rectangle(elXPos, elYPos, elWidth, elHeight), "halign=center\nDouble-click on an element to add it to the diagram\n\nImport uxf Files using the Menu \"Import\" or simply drag them into the diagram\n\nSave diagrams persistent in browser storage using the \"Save\" menu", "");
		((GwtComponent) emptyElement.getComponent()).drawOn(canvas.getContext2d(), false);

	}

	public void setMinSize(int minWidth, int minHeight) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
	}

	public void setScrollPanel(AutoResizeScrollDropPanel scrollPanel) {
		this.scrollPanel = scrollPanel;
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
