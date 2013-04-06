package com.baselet.gwt.client.view;

import java.util.Set;

import com.baselet.control.enumerations.Direction;
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
		private int moveStartX, moveStartY;
		private GridElement elementToDrag;
	}

	public interface MouseDragHandler {
		void onMouseDrag(int diffX, int diffY, GridElement gridElement);
		void onMouseDown(GridElement gridElement);
	}

	public static void addMouseDragHandler(final DrawPanelCanvas drawPanelCanvas, final MouseDragHandler mouseDragHandler) {
		final DragCache storage = new DragCache();

		drawPanelCanvas.getCanvas().addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault(); // necessary to avoid Chrome showing the text-cursor during dragging
				storage.moveStartX = event.getX(); //getClientX also works on zoomed browser (getScreenX moves elements too slow)
				storage.moveStartY = event.getY();
				storage.dragging = true;
				storage.elementToDrag = drawPanelCanvas.getGridElementOnPosition(storage.moveStartX, storage.moveStartY);
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
				Utils.showCursor(Style.Cursor.AUTO);
			}
		});
		drawPanelCanvas.getCanvas().addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (storage.dragging) {
					int diffX = event.getX() - storage.moveStartX;
					diffX -= (diffX % DrawPanelCanvas.GRID_SIZE);

					int diffY = event.getY() - storage.moveStartY;
					diffY -= (diffY % DrawPanelCanvas.GRID_SIZE);

					if (diffX != 0 || diffY != 0) {
						mouseDragHandler.onMouseDrag(diffX, diffY, storage.elementToDrag);
						storage.moveStartX += diffX;
						storage.moveStartY += diffY;
					}
				}
				GridElement geOnPosition = drawPanelCanvas.getGridElementOnPosition(event.getX(), event.getY());
				if (geOnPosition != null) {
					Set<Direction> resizeDirection = geOnPosition.getResizeArea(event.getX() - geOnPosition.getRectangle().getX(), event.getY() - geOnPosition.getRectangle().getY());
					if (resizeDirection.isEmpty()) {
						Utils.showCursor(Style.Cursor.POINTER); // HAND Cursor
					} else if (resizeDirection.contains(Direction.UP) && resizeDirection.contains(Direction.RIGHT)) {
						Utils.showCursor(Style.Cursor.NE_RESIZE);
					} else if (resizeDirection.contains(Direction.UP) && resizeDirection.contains(Direction.LEFT)) {
						Utils.showCursor(Style.Cursor.NW_RESIZE);
					} else if (resizeDirection.contains(Direction.DOWN) && resizeDirection.contains(Direction.LEFT)) {
						Utils.showCursor(Style.Cursor.SW_RESIZE);
					} else if (resizeDirection.contains(Direction.DOWN) && resizeDirection.contains(Direction.RIGHT)) {
						Utils.showCursor(Style.Cursor.SE_RESIZE);
					} else if (resizeDirection.contains(Direction.UP)) {
						Utils.showCursor(Style.Cursor.N_RESIZE);
					} else if (resizeDirection.contains(Direction.RIGHT)) {
						Utils.showCursor(Style.Cursor.E_RESIZE);
					} else if (resizeDirection.contains(Direction.DOWN)) {
						Utils.showCursor(Style.Cursor.S_RESIZE);
					} else if (resizeDirection.contains(Direction.LEFT)) {
						Utils.showCursor(Style.Cursor.W_RESIZE);
					}
				} else if (storage.dragging) { // Dragging the whole diagram
					Utils.showCursor(Style.Cursor.POINTER); // HAND Cursor
				}
				else Utils.showCursor(Style.Cursor.AUTO);
			}
		});
	}

}
