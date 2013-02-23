package com.baselet.client;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusWidget;

public class MouseDragUtils {
	
	private static class DragCache {
		private boolean dragging;
		private HandlerRegistration mouseMove;
		private int moveStartX, moveStartY;
		private GridElement elementToDrag;
	}
	
	public interface MouseDragHandler {
		void onMouseDrag(int diffX, int diffY, GridElement gridElement);
	}

	public static void addMouseDragHandler(final DrawPanelCanvas drawPanelCanvas, final MouseDragHandler mouseDragHandler) {
		final DragCache storage = new DragCache();
		
		drawPanelCanvas.getCanvas().addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				storage.moveStartX = event.getX(); //getClientX also works on zoomed browser (getScreenX moves elements too slow)
				storage.moveStartY = event.getY();
				storage.dragging = true;
				storage.elementToDrag = drawPanelCanvas.getGridElementOnPosition(storage.moveStartX, storage.moveStartY);
				// do other stuff related to starting of "dragging"
				storage.mouseMove = drawPanelCanvas.getCanvas().addMouseMoveHandler(new MouseMoveHandler() {
					@Override
					public void onMouseMove(MouseMoveEvent event) {
						if (storage.dragging) {
							int diffX = event.getX() - storage.moveStartX;
							diffX -= (diffX % DrawPanelCanvas.GRID_SIZE);

							int diffY = event.getY() - storage.moveStartY;
							diffY -= (diffY % DrawPanelCanvas.GRID_SIZE);
							
//							System.out.println("REAL " + event.getX() + " MODIFIED " + diffX);
							
							if (diffX != 0 || diffY != 0) {
								mouseDragHandler.onMouseDrag(diffX, diffY, storage.elementToDrag);
								storage.moveStartX += diffX;
								storage.moveStartY += diffY;
							}
						}
					}
				});
			}
		});
		drawPanelCanvas.getCanvas().addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				endDragging(storage);
			}
		});
		drawPanelCanvas.getCanvas().addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				endDragging(storage);
			}
		});
	}

	private static void endDragging(final DragCache storage) {
		if (storage.dragging){
			storage.dragging = false;
			storage.mouseMove.removeHandler();
		}
	}

}
