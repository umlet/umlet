package com.baselet.gwt.client.view;

import java.util.Arrays;

import com.baselet.control.NewGridElementConstants;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Element;
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
		final Element canvEl = drawPanelCanvas.getElement();
		final DragCache storage = new DragCache();
		canvas.addTouchStartHandler(new TouchStartHandler() {
			@Override
			public void onTouchStart(TouchStartEvent event) {
				if (event.getTouches().length() == 1) { // only handle single finger touches (to allow zooming with 2 fingers)
					handleStart(drawPanelCanvas, mouseDragHandler, storage, event, getTouchPoint(canvEl, event));
				}
			}
		});
		canvas.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				handleStart(drawPanelCanvas, mouseDragHandler, storage, event, getMousePoint(canvEl, event));
			}
		});

		canvas.addTouchEndHandler(new TouchEndHandler() {
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				handleEnd(storage, mouseDragHandler);
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				handleEnd(storage, mouseDragHandler);
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				handleEnd(storage, mouseDragHandler);
			}
		});

		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				handleMove(drawPanelCanvas, mouseDragHandler, storage, event, getMousePoint(canvEl, event));
			}
		});
		canvas.addTouchMoveHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				if (event.getTouches().length() == 1) { // only handle single finger touches (to allow zooming with 2 fingers)
					handleMove(drawPanelCanvas, mouseDragHandler, storage, event, getTouchPoint(canvEl, event));
				}
			}
		});
	}

	private static void handleEnd(final DragCache storage, MouseDragHandler mouseDragHandler) {
//		Notification.showInfo("UP");
		if (Arrays.asList(DragStatus.FIRST, DragStatus.CONTINUOUS).contains(storage.dragging)) {
			mouseDragHandler.onMouseDragEnd(storage.elementToDrag);
		}
		storage.dragging = DragStatus.NO;
	}

	private static void handleStart(final DrawFocusPanel drawPanelCanvas, final MouseDragHandler mouseDragHandler, final DragCache storage, HumanInputEvent<?> event, Point p) {
//		Notification.showInfo("DOWN " + p.x);
		storage.moveStart = new Point(p.x, p.y);
		storage.dragging = DragStatus.FIRST;
		storage.elementToDrag = drawPanelCanvas.getGridElementOnPosition(storage.moveStart);
		mouseDragHandler.onMouseDown(storage.elementToDrag, event.isControlKeyDown());
	}

	private static void handleMove(final DrawFocusPanel drawPanelCanvas, final MouseDragHandler mouseDragHandler, final DragCache storage, HumanInputEvent<?> event, Point p) {
//		Notification.showInfo("MOVE " + p.x);
		event.preventDefault(); // necessary to avoid Chrome showing the text-cursor during dragging
		if (Arrays.asList(DragStatus.FIRST, DragStatus.CONTINUOUS).contains(storage.dragging)) {
			int diffX = p.x - storage.moveStart.getX();
			int diffY = p.y - storage.moveStart.getY();
			diffX -= (diffX % NewGridElementConstants.DEFAULT_GRID_SIZE);
			diffY -= (diffY % NewGridElementConstants.DEFAULT_GRID_SIZE);
			if (diffX != 0 || diffY != 0) {
				mouseDragHandler.onMouseMoveDragging(storage.moveStart, diffX, diffY, storage.elementToDrag, event.isShiftKeyDown(), event.isControlKeyDown(), (storage.dragging == DragStatus.FIRST));
				storage.dragging = DragStatus.CONTINUOUS; // after FIRST real drag switch to CONTINUOUS
				storage.moveStart.move(diffX, diffY);
			}
		}
		else {
			mouseDragHandler.onMouseMove(p);
		}
	}

	private static Point getMousePoint(final Element canvEl, MouseEvent<?> event) {
		return new Point(event.getRelativeX(canvEl), event.getRelativeY(canvEl));
	}

	private static Point getTouchPoint(final Element canvEl, TouchEvent<?> event) {
		return new Point(event.getTouches().get(0).getRelativeX(canvEl), event.getTouches().get(0).getRelativeY(canvEl));
	}
}
