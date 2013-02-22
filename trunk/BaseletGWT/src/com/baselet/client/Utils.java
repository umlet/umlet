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

public class Utils {
	
	private static class Storage {
		private boolean dragging;
		private HandlerRegistration mouseMove;
		private int moveStartX, moveStartY;
	}
	
	public interface MouseDragHandler {
		void onMouseDrag(int diffX, int diffY);
	}


	public static void addMouseDragHandler(final FocusWidget widget, final MouseDragHandler mouseDragHandler) {
		final Storage storage = new Storage();
		
		widget.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				storage.moveStartX = event.getScreenX();
				storage.moveStartY = event.getScreenY();
				System.out.println("START " + storage.moveStartX + " /" + storage.moveStartY);
				storage.dragging = true;
				// do other stuff related to starting of "dragging"
				storage.mouseMove = widget.addMouseMoveHandler(new MouseMoveHandler() {
					@Override
					public void onMouseMove(MouseMoveEvent event) {
						if (storage.dragging) {
							int diffX = event.getScreenX() - storage.moveStartX;
							int diffY = event.getScreenY() - storage.moveStartY;
							System.out.println("NOW " + diffX + " /" + diffY);
							mouseDragHandler.onMouseDrag(diffX, diffY);
							storage.moveStartX = event.getScreenX();
							storage.moveStartY = event.getScreenY();
						}
					}
				});
			}
		});
		widget.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				endDragging(storage);
			}
		});
		widget.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				endDragging(storage);
			}
		});
	}

	private static void endDragging(final Storage storage) {
		if (storage.dragging){
			storage.dragging = false;
			storage.mouseMove.removeHandler();
		}
	}

}
