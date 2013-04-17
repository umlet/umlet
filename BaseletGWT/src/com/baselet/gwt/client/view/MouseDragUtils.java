package com.baselet.gwt.client.view;

import java.util.Set;

import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.baselet.gwt.client.Utils;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

public class MouseDragUtils {

	private static class DragCache {
		private boolean dragging;
		private Point moveStart;
		private GridElement elementToDrag;
	}

	public interface MouseDragHandler {
		void onMouseMoveDragging(Point dragStart, int diffX, int diffY, GridElement draggedGridElement, boolean isShiftKeyDown);
		void onMouseOut();
		void onMouseDown(GridElement gridElement);
		void onMouseMove(Point absolute);
	}

	public static void addMouseDragHandler(final DrawPanelCanvas drawPanelCanvas, final MouseDragHandler mouseDragHandler) {
		final DragCache storage = new DragCache();

		drawPanelCanvas.getCanvas().addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault(); // necessary to avoid Chrome showing the text-cursor during dragging
				storage.moveStart = new Point(event.getX(), event.getY());
				storage.dragging = true;
				storage.elementToDrag = drawPanelCanvas.getGridElementOnPosition(storage.moveStart);
				mouseDragHandler.onMouseDown(storage.elementToDrag);
			}
		});
		drawPanelCanvas.getCanvas().addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				storage.dragging = false;
			}
		});
		drawPanelCanvas.getCanvas().addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				storage.dragging = false;
				mouseDragHandler.onMouseOut();
			}
		});
		drawPanelCanvas.getCanvas().addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (storage.dragging) {
					int diffX = event.getX() - storage.moveStart.getX();
					int diffY = event.getY() - storage.moveStart.getY();
					diffX -= (diffX % DrawPanelCanvas.GRID_SIZE);
					diffY -= (diffY % DrawPanelCanvas.GRID_SIZE);
					if (diffX != 0 || diffY != 0) {
						mouseDragHandler.onMouseMoveDragging(storage.moveStart, diffX, diffY, storage.elementToDrag, event.isShiftKeyDown());
						storage.moveStart.move(diffX, diffY);
					}
				} else {
					mouseDragHandler.onMouseMove(new Point(event.getX(), event.getY()));
				}
			}
		});
	}

}
