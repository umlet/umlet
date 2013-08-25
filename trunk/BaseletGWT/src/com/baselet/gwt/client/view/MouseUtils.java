package com.baselet.gwt.client.view;

import java.util.Arrays;

import com.baselet.control.NewGridElementConstants;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
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
import com.google.gwt.user.client.ui.FocusPanel;

public class MouseUtils {

	private static enum DragStatus {
		FIRST, CONTINUOUS, NO
	}

	private static class DragCache {
		private DragStatus dragging = DragStatus.NO;
		private Point moveStart;
		private GridElement elementToDrag;
		private DrawFocusPanel activePanel;
		private DrawFocusPanel mouseContainingPanel;
		private boolean touchDetected = false;
		/**
		 * doubleclicks are only handled if the mouse has moved into the canvas before
		 * this is necessary to void unwanted propagation of suggestbox-selections via doubleclick
		 * TODO: a better fix would be a custom SuggestDisplay which stops mouseevent propagation after handling them
		 */
		private boolean doubleClickEnabled = true;

		private Timer menuShowTimer;
	}

	public static void addMouseHandler(FocusPanel handlerTarget, final DrawFocusPanel ... panels) {
		final DragCache storage = new DragCache();

		for (final DrawFocusPanel panel : panels) {
			panel.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					storage.mouseContainingPanel = null;
				}
			});
			panel.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					storage.mouseContainingPanel = panel;
				}
			});
		}

		handlerTarget.addTouchStartHandler(new TouchStartHandler() {
			@Override
			public void onTouchStart(final TouchStartEvent event) {
				storage.touchDetected = true;
				if (event.getTouches().length() == 1) { // only handle single finger touches (to allow zooming with 2 fingers)
					final Point absolutePos = getPointAbsolute(event);
					storage.activePanel = getPanelWhichContainsPoint(panels, absolutePos);
					if (storage.activePanel != null) {
						handleStart(panels, storage, event, getPoint(storage.activePanel, event));
					}
					//					Notification.showInfo("START " + absolutePos.x);
					storage.menuShowTimer = new Timer() {
						@Override
						public void run() {
							handleShowMenu(storage.activePanel, absolutePos);
						}
					};
					storage.menuShowTimer.schedule(1000);
				}
			}
		});

		handlerTarget.addTouchEndHandler(new TouchEndHandler() {
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				storage.menuShowTimer.cancel();
				if (storage.activePanel != null) {
					handleEnd(storage, storage.activePanel, getPoint(storage.activePanel, event));
				}
			}
		});
		handlerTarget.addTouchMoveHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				storage.menuShowTimer.cancel();
				if (event.getTouches().length() == 1) { // only handle single finger touches (to allow zooming with 2 fingers)
					handleMove(storage.activePanel, storage, event);
				}
			}
		});

		handlerTarget.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!storage.touchDetected) {
					storage.activePanel = getPanelWhichContainsPoint(panels, getPointAbsolute(event));
					if (storage.activePanel != null) {
						handleStart(panels, storage, event, getPoint(storage.activePanel, event));
					}
				}
			}
		});

		handlerTarget.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (!storage.touchDetected && storage.activePanel != null) {
					handleEnd(storage, storage.activePanel, getPoint(storage.activePanel, event));
				}
			}
		});
		handlerTarget.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (!storage.touchDetected && storage.activePanel != null) {
					handleEnd(storage, storage.activePanel, getPoint(storage.activePanel, event));
					storage.doubleClickEnabled = false;
				}
			}
		});
		handlerTarget.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				storage.doubleClickEnabled = true;
			}
		});

		handlerTarget.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (!storage.touchDetected) { // mousemove is triggered on each touchdown therefore it must be deactivated if touch is detected
					event.preventDefault(); // necessary to avoid Chrome showing the text-cursor during dragging
					handleMove(storage.activePanel, storage, event);
				}
			}
		});

		// double tap on mobile devices is not easy to implement because browser zoom on double-tap is not an event which can be canceled
		handlerTarget.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (!storage.touchDetected && storage.activePanel != null) {
					if (storage.doubleClickEnabled) {
						handleDoubleClick(storage.activePanel, getPoint(storage.activePanel, event));
					}
				}
			}
		});

		handlerTarget.addDomHandler(new ContextMenuHandler() {
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				if (storage.activePanel != null) {
					event.preventDefault();
					event.stopPropagation();
					handleShowMenu(storage.activePanel, new Point(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY()));
				}
			}
		}, ContextMenuEvent.getType());
	}

	private static void handleEnd(final DragCache storage, DrawFocusPanel drawPanelCanvas, Point point) {
		//		Notification.showInfo("UP");
		if (Arrays.asList(DragStatus.FIRST, DragStatus.CONTINUOUS).contains(storage.dragging)) {
			drawPanelCanvas.onMouseDragEnd(storage.elementToDrag, point);
		}
		storage.dragging = DragStatus.NO;
	}

	private static void handleStart(DrawFocusPanel[] panels, final DragCache storage, HumanInputEvent<?> event, Point p) {
		//		Notification.showInfo("DOWN " + p.x);
		event.preventDefault(); // necessary to avoid Chrome showing the text-cursor during dragging
		storage.moveStart = new Point(p.x, p.y);
		storage.dragging = DragStatus.FIRST;
		storage.elementToDrag = storage.activePanel.getGridElementOnPosition(storage.moveStart);
		storage.activePanel.onMouseDown(storage.elementToDrag, event.isControlKeyDown());
	}

	private static DrawFocusPanel getPanelWhichContainsPoint(DrawFocusPanel[] panels, Point p) {
		DrawFocusPanel returnPanel = null;
		for (DrawFocusPanel panel : panels) {
			Rectangle visibleBounds = panel.getVisibleBounds();
			visibleBounds.move(panel.getAbsoluteLeft(), panel.getAbsoluteTop());
			if (visibleBounds.contains(p)) {
				panel.setFocus(true);
				returnPanel = panel;
			} else {
				panel.setFocus(false);
			}
		}
		return returnPanel;
	}

	private static void handleMove(final DrawFocusPanel drawPanelCanvas, final DragCache storage, HumanInputEvent<?> event) {
		//		Notification.showInfo("MOVE " + getPointAbsolute(event));
		if (storage.activePanel != null && Arrays.asList(DragStatus.FIRST, DragStatus.CONTINUOUS).contains(storage.dragging)) {
			Point p = getPoint(storage.activePanel, event);
			int diffX = p.x - storage.moveStart.getX();
			int diffY = p.y - storage.moveStart.getY();
			diffX -= (diffX % NewGridElementConstants.DEFAULT_GRID_SIZE);
			diffY -= (diffY % NewGridElementConstants.DEFAULT_GRID_SIZE);
			if (diffX != 0 || diffY != 0) {
				drawPanelCanvas.onMouseMoveDragging(storage.moveStart, diffX, diffY, storage.elementToDrag, event.isShiftKeyDown(), event.isControlKeyDown(), (storage.dragging == DragStatus.FIRST));
				storage.dragging = DragStatus.CONTINUOUS; // after FIRST real drag switch to CONTINUOUS
				storage.moveStart.move(diffX, diffY);
			}
		}
		else if (storage.mouseContainingPanel != null) {
			storage.mouseContainingPanel.onMouseMove(getPoint(storage.mouseContainingPanel, event));
		}
	}

	private static void handleDoubleClick(final DrawFocusPanel drawPanelCanvas, Point p) {
		drawPanelCanvas.onDoubleClick(drawPanelCanvas.getGridElementOnPosition(p));
	}

	private static void handleShowMenu(final DrawFocusPanel drawPanelCanvas, Point p) {
		drawPanelCanvas.onShowMenu(p);
	}

	private static Point getPoint(DrawFocusPanel drawPanelCanvas, HumanInputEvent<?> event) {
		Element e = drawPanelCanvas.getElement();
		if (event instanceof MouseEvent<?>) {
			return new Point(((MouseEvent<?>) event).getRelativeX(e), ((MouseEvent<?>) event).getRelativeY(e));
		} else if (event instanceof TouchEvent<?>) {
			return new Point(((TouchEvent<?>) event).getTouches().get(0).getRelativeX(e), ((TouchEvent<?>) event).getTouches().get(0).getRelativeY(e));
		} else {
			throw new RuntimeException("Unknown Event Type: " + event);
		}
	}

	private static Point getPointAbsolute(HumanInputEvent<?> event) {
		if (event instanceof MouseEvent<?>) {
			return new Point(((MouseEvent<?>) event).getClientX(), ((MouseEvent<?>) event).getClientY());
		} else if (event instanceof TouchEvent<?>) {
			return new Point(((TouchEvent<?>) event).getTouches().get(0).getPageX(), ((TouchEvent<?>) event).getTouches().get(0).getPageY());
		} else {
			throw new RuntimeException("Unknown Event Type: " + event);
		}
	}
}
