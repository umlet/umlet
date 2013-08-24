package com.baselet.gwt.client.view;

import java.util.Arrays;

import com.baselet.control.NewGridElementConstants;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusWidget;

public class MouseUtils {

	private static enum DragStatus {
		FIRST, CONTINUOUS, NO
	}

	private static class DragCache {
		private DragStatus dragging = DragStatus.NO;
		private Point moveStart;
		private GridElement elementToDrag;

		/**
		 * doubleclicks are only handled if the mouse has moved into the canvas before
		 * this is necessary to void unwanted propagation of suggestbox-selections via doubleclick
		 * TODO: a better fix would be a custom SuggestDisplay which stops mouseevent propagation after handling them
		 */
		private boolean doubleClickEnabled = true;

		private Timer menuShowTimer;
	}

	public interface MouseDragHandler {
		void onMouseMoveDragging(Point dragStart, int diffX, int diffY, GridElement draggedGridElement, boolean isShiftKeyDown, boolean isCtrlKeyDown, boolean firstDrag);

		void onMouseDragEnd(GridElement gridElement, Point lastPoint);

		void onMouseDown(GridElement gridElement, boolean controlKeyDown);

		void onMouseMove(Point absolute);

		void onDoubleClick(GridElement ge);

		void onShowMenu(Point point);
	}

	public static void addMouseHandler(final DrawFocusPanel drawPanelCanvas, FocusWidget canvas, final MouseDragHandler handler) {
		final Element canvEl = drawPanelCanvas.getElement();
		final DragCache storage = new DragCache();

		canvas.addTouchStartHandler(new TouchStartHandler() {
			@Override
			public void onTouchStart(final TouchStartEvent event) {
				if (event.getTouches().length() == 1) { // only handle single finger touches (to allow zooming with 2 fingers)
					handleStart(drawPanelCanvas, handler, storage, event, getTouchPoint(canvEl, event));

					final Point absolutePos = new Point(event.getTouches().get(0).getPageX(), event.getTouches().get(0).getPageY());
//					Notification.showInfo("START " + absolutePos.x);
					storage.menuShowTimer = new Timer() {
						@Override
						public void run() {
							handleShowMenu(handler, absolutePos);
						}
					};
					storage.menuShowTimer.schedule(500);
				}
			}
		});
		canvas.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				handleStart(drawPanelCanvas, handler, storage, event, getMousePoint(canvEl, event));
			}
		});

		canvas.addTouchEndHandler(new TouchEndHandler() {
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				storage.menuShowTimer.cancel();
				handleEnd(storage, handler, getTouchPoint(canvEl, event));
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				handleEnd(storage, handler, getMousePoint(canvEl, event));
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				handleEnd(storage, handler, getMousePoint(canvEl, event));
				storage.doubleClickEnabled = false;
			}
		});
		canvas.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				storage.doubleClickEnabled = true;
			}
		});

		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				handleMove(drawPanelCanvas, handler, storage, event, getMousePoint(canvEl, event));
			}
		});
		canvas.addTouchMoveHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				storage.menuShowTimer.cancel();
				if (event.getTouches().length() == 1) { // only handle single finger touches (to allow zooming with 2 fingers)
					handleMove(drawPanelCanvas, handler, storage, event, getTouchPoint(canvEl, event));
				}
			}
		});

		// double tap on mobile devices is not easy to implement because browser zoom on double-tap is not an event which can be canceled
		canvas.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (storage.doubleClickEnabled) {
					handleDoubleClick(drawPanelCanvas, handler, getMousePoint(canvEl, event));
				}
			}
		});

		canvas.addDomHandler(new ContextMenuHandler() {
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
				handleShowMenu(handler, new Point(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY()));
			}
		}, ContextMenuEvent.getType());
	}

	private static void handleEnd(final DragCache storage, MouseDragHandler mouseDragHandler, Point point) {
		//		Notification.showInfo("UP");
		if (Arrays.asList(DragStatus.FIRST, DragStatus.CONTINUOUS).contains(storage.dragging)) {
			mouseDragHandler.onMouseDragEnd(storage.elementToDrag, point);
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

	private static void handleDoubleClick(final DrawFocusPanel drawPanelCanvas, final MouseDragHandler handler, Point p) {
		handler.onDoubleClick(drawPanelCanvas.getGridElementOnPosition(p));
	}

	private static void handleShowMenu(final MouseDragHandler handler, Point p) {
		handler.onShowMenu(p);
	}

	private static Point getMousePoint(final Element canvEl, MouseEvent<?> event) {
		return new Point(event.getRelativeX(canvEl), event.getRelativeY(canvEl));
	}

	private static Point getTouchPoint(final Element canvEl, TouchEvent<?> event) {
		return new Point(event.getTouches().get(0).getRelativeX(canvEl), event.getTouches().get(0).getRelativeY(canvEl));
	}
}
