package com.baselet.gwt.client.view;

import java.util.Arrays;

import com.baselet.control.NewGridElementConstants;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.FocusWidget;

public class MouseUtils {

	private static enum DragStatus {
		FIRST, CONTINUOUS, NO
	}

	private static class DragCache {
		private DragStatus dragging = DragStatus.NO;
		private Point moveStart;
		private GridElement elementToDrag;
	}

	public interface MouseDragHandler {
		void onMouseMoveDragging(Point dragStart, int diffX, int diffY, GridElement draggedGridElement, boolean isShiftKeyDown, boolean isCtrlKeyDown, boolean firstDrag);

		void onMouseDragEnd(GridElement gridElement);

		void onMouseDown(GridElement gridElement, boolean controlKeyDown);

		void onMouseMove(Point absolute);
	}

	public static void addMouseHandler(final DrawFocusPanel drawPanelCanvas, FocusWidget canvas, final MouseDragHandler mouseDragHandler) {
		final DragCache storage = new DragCache();

		canvas.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault(); // necessary to avoid Chrome showing the text-cursor during dragging
				storage.moveStart = new Point(event.getX(), event.getY());
				storage.dragging = DragStatus.FIRST;
				storage.elementToDrag = drawPanelCanvas.getGridElementOnPosition(storage.moveStart);
				mouseDragHandler.onMouseDown(storage.elementToDrag, event.isControlKeyDown());
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				handleDragEnd(storage, mouseDragHandler);
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				handleDragEnd(storage, mouseDragHandler);
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (Arrays.asList(DragStatus.FIRST, DragStatus.CONTINUOUS).contains(storage.dragging)) {
					int diffX = event.getX() - storage.moveStart.getX();
					int diffY = event.getY() - storage.moveStart.getY();
					diffX -= (diffX % NewGridElementConstants.DEFAULT_GRID_SIZE);
					diffY -= (diffY % NewGridElementConstants.DEFAULT_GRID_SIZE);
					if (diffX != 0 || diffY != 0) {
						mouseDragHandler.onMouseMoveDragging(storage.moveStart, diffX, diffY, storage.elementToDrag, event.isShiftKeyDown(), event.isControlKeyDown(), (storage.dragging == DragStatus.FIRST));
						storage.dragging = DragStatus.CONTINUOUS; // after FIRST real drag switch to CONTINUOUS
						storage.moveStart.move(diffX, diffY);
					}
				}
				else {
					mouseDragHandler.onMouseMove(new Point(event.getX(), event.getY()));
				}
			}
		});
	}

	private static void handleDragEnd(final DragCache storage, MouseDragHandler mouseDragHandler) {
		if (Arrays.asList(DragStatus.FIRST, DragStatus.CONTINUOUS).contains(storage.dragging)) {
			mouseDragHandler.onMouseDragEnd(storage.elementToDrag);
		}
		storage.dragging = DragStatus.NO;
	}

}
