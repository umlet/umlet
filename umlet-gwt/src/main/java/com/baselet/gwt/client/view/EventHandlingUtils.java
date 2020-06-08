package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;
import com.baselet.element.interfaces.GridElement;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusPanel;

public class EventHandlingUtils {

	private static final List<DragStatus> DRAG_COMMANDS = Arrays.asList(DragStatus.FIRST, DragStatus.CONTINUOUS);

	public static interface EventHandlingTarget {

		HandlerRegistration addMouseOutHandler(MouseOutHandler mouseOutHandler);

		HandlerRegistration addMouseOverHandler(MouseOverHandler mouseOverHandler);

		void handleKeyDown(KeyDownEvent event);

		void handleKeyUp(KeyUpEvent event);

		void onMouseMoveDraggingScheduleDeferred(Point moveStart, int diffX, int diffY, GridElement elementToDrag, boolean shiftKeyDown, boolean controlKeyDown, boolean b);

		void onMouseMove(Point point);

		void onMouseDragEnd(GridElement elementToDrag, Point point);

		void onMouseDownScheduleDeferred(GridElement elementToDrag, boolean controlKeyDown);

		void onDoubleClick(GridElement gridElementOnPosition);

		void onShowMenu(Point p);

		Rectangle getVisibleBounds();

		int getAbsoluteLeft();

		int getAbsoluteTop();

		void setFocus(boolean b);

		GridElement getGridElementOnPosition(Point p);

		Element getElement();

	}

	private static enum DragStatus {
		FIRST, CONTINUOUS, NO
	}

	private static class DragCache {
		private DragStatus dragging = DragStatus.NO;
		private Point moveStart;
		private GridElement elementToDrag;
		private EventHandlingTarget activePanel;
		private EventHandlingTarget mouseContainingPanel;
		private List<HandlerRegistration> nonTouchHandlers = new ArrayList<HandlerRegistration>();
		/**
		 * doubleclicks are only handled if the mouse has moved into the canvas before
		 * this is necessary to void unwanted propagation of suggestbox-selections via doubleclick
		 * TODO: a better fix would be a custom SuggestDisplay which stops mouseevent propagation after handling them
		 */
		private boolean doubleClickEnabled = true;

		// private Timer menuShowTimer; //TODO doesn't really work at the moment (because some move and end events are not processed, therefore it's shown even if not wanted)
	}

	public static void addEventHandler(final FocusPanel handlerTarget, final EventHandlingTarget... panels) {
		final DragCache storage = new DragCache();

		for (final EventHandlingTarget panel : panels) {
			storage.nonTouchHandlers.add(panel.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					storage.mouseContainingPanel = null;
				}
			}));
			storage.nonTouchHandlers.add(panel.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					storage.mouseContainingPanel = panel;
				}
			}));
		}

		handlerTarget.addTouchStartHandler(new TouchStartHandler() {
			@Override
			public void onTouchStart(final TouchStartEvent event) {
				// some mouseevents are interfering with touch events (eg: mousemove is triggered on each touchdown event) therefore they are removed as soon as a touch event is detected
				if (storage.nonTouchHandlers != null) {
					for (HandlerRegistration h : storage.nonTouchHandlers) {
						h.removeHandler();
					}
					storage.nonTouchHandlers = null;
				}
				if (event.getTouches().length() == 1) { // only handle single finger touches (to allow zooming with 2 fingers)
					final Point absolutePos = getPointAbsolute(event);
					storage.activePanel = getPanelWhichContainsPoint(panels, absolutePos);
					if (storage.activePanel != null) {
						handleStart(panels, storage, handlerTarget, event, getPoint(storage.activePanel, event));
					}
					// storage.menuShowTimer = new Timer() {
					// @Override
					// public void run() {
					// handleShowMenu(storage.activePanel, absolutePos);
					// }
					// };
					// storage.menuShowTimer.schedule(1000);
				}
			}
		});

		handlerTarget.addTouchEndHandler(new TouchEndHandler() {
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				// storage.menuShowTimer.cancel();
				if (storage.activePanel != null) {
					handleEnd(storage.activePanel, storage, event);
				}
			}
		});
		handlerTarget.addTouchMoveHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				// storage.menuShowTimer.cancel();
				if (event.getTouches().length() == 1) { // only handle single finger touches (to allow zooming with 2 fingers)
					handleMove(storage.activePanel, storage, event);
				}
			}
		});

		storage.nonTouchHandlers.add(handlerTarget.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				storage.activePanel = getPanelWhichContainsPoint(panels, getPointAbsolute(event));
				if (storage.activePanel != null) {
					handleStart(panels, storage, handlerTarget, event, getPoint(storage.activePanel, event));
				}
			}
		}));

		storage.nonTouchHandlers.add(handlerTarget.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (storage.activePanel != null) {
					handleEnd(storage.activePanel, storage, event);
				}
			}
		}));
		storage.nonTouchHandlers.add(handlerTarget.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (storage.activePanel != null) {
					handleEnd(storage.activePanel, storage, event);
					storage.doubleClickEnabled = false;
				}
			}
		}));
		storage.nonTouchHandlers.add(handlerTarget.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				storage.doubleClickEnabled = true;
			}
		}));

		storage.nonTouchHandlers.add(handlerTarget.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				handleMove(storage.activePanel, storage, event);
			}
		}));

		// double tap on mobile devices is not easy to implement because browser zoom on double-tap is not an event which can be canceled
		storage.nonTouchHandlers.add(handlerTarget.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (storage.activePanel != null) {
					if (storage.doubleClickEnabled) {
						handleDoubleClick(storage.activePanel, getPoint(storage.activePanel, event));
					}
				}
			}
		}));

		storage.nonTouchHandlers.add(handlerTarget.addDomHandler(new ContextMenuHandler() {
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				if (storage.activePanel != null) {
					event.preventDefault(); // avoid default contextmenu
					event.stopPropagation();
					handleShowMenu(storage.activePanel, new Point(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY()));
				}
			}
		}, ContextMenuEvent.getType()));

		storage.nonTouchHandlers.add(handlerTarget.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (storage.activePanel != null) {
					storage.activePanel.handleKeyDown(event);
				}
			}
		}));
		storage.nonTouchHandlers.add(handlerTarget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (storage.activePanel != null) {
					storage.activePanel.handleKeyUp(event);
				}
			}
		}));

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				handlerTarget.setFocus(true); // set focus to enable keyboard shortcuts

			}
		});
	}

	private static void handleEnd(EventHandlingTarget panel, final DragCache storage, HumanInputEvent<?> event) {
		if(event.getNativeEvent().getButton() != 1 && event.getNativeEvent().getButton() != 2)
			return;
		if (event.getNativeEvent().getButton() == 1 || event.getNativeEvent().getButton() == 2)
		{
			// Notification.showInfo("UP");
			if (DRAG_COMMANDS.contains(storage.dragging)) {
				panel.onMouseDragEnd(storage.elementToDrag, getPoint(storage.activePanel, event));
			}
			storage.dragging = DragStatus.NO;
		}

	}

	private static void handleStart(EventHandlingTarget[] panels, final DragCache storage, FocusPanel handlerTarget, HumanInputEvent<?> event, Point p) {
		if(event.getNativeEvent().getButton() == 1 || event.getNativeEvent().getButton() == 2)
		{
		// Notification.showInfo("DOWN " + p.x);
		handlerTarget.setFocus(true);

		event.preventDefault(); // necessary to avoid showing textcursor and selecting proppanel in chrome AND to avoid scrolling with touch move (problem is it also avoids scrolling with 2 fingers)
		storage.moveStart = new Point(p.x, p.y);
		storage.dragging = DragStatus.FIRST;
		storage.elementToDrag = storage.activePanel.getGridElementOnPosition(storage.moveStart);

			storage.activePanel.onMouseDownScheduleDeferred(storage.elementToDrag, event.isControlKeyDown());
		} else {
			if ( storage.activePanel instanceof DrawPanelPalette)
			{
				((DrawPanelPalette) storage.activePanel).CancelDragNoDuplicate();
			}
			if ( storage.activePanel instanceof DrawPanelDiagram)
			{
				((DrawPanelDiagram) storage.activePanel).CancelDragOfPalette();
			}
		}
	}

	private static void handleMove(final EventHandlingTarget panel, final DragCache storage, HumanInputEvent<?> event) {
		// Notification.showInfo("MOVE " + getPointAbsolute(event));
		if (storage.activePanel != null && DRAG_COMMANDS.contains(storage.dragging)) {
			Point p = getPoint(storage.activePanel, event);
			int diffX = p.x - storage.moveStart.getX();
			int diffY = p.y - storage.moveStart.getY();
			diffX -= diffX % SharedConstants.DEFAULT_GRID_SIZE;
			diffY -= diffY % SharedConstants.DEFAULT_GRID_SIZE;
			if (diffX != 0 || diffY != 0) {
				panel.onMouseMoveDraggingScheduleDeferred(storage.moveStart, diffX, diffY, storage.elementToDrag, event.isShiftKeyDown(), event.isControlKeyDown(), storage.dragging == DragStatus.FIRST);
				storage.dragging = DragStatus.CONTINUOUS; // after FIRST real drag switch to CONTINUOUS
				storage.moveStart = storage.moveStart.copy().move(diffX, diffY); // make copy because otherwise deferred action will act on wrong position
			}
		}
		else if (storage.mouseContainingPanel != null) {
			storage.mouseContainingPanel.onMouseMove(getPoint(storage.mouseContainingPanel, event));
		}
	}

	private static void handleDoubleClick(final EventHandlingTarget panel, Point p) {
		panel.onDoubleClick(panel.getGridElementOnPosition(p));
	}

	private static void handleShowMenu(final EventHandlingTarget panel, Point p) {
		panel.onShowMenu(p);
	}

	private static EventHandlingTarget getPanelWhichContainsPoint(EventHandlingTarget[] panels, Point p) {
		EventHandlingTarget returnPanel = null;
		for (EventHandlingTarget panel : panels) {
			Rectangle visibleBounds = panel.getVisibleBounds();
			visibleBounds.move(panel.getAbsoluteLeft(), panel.getAbsoluteTop());
			if (visibleBounds.contains(p)) {
				panel.setFocus(true);
				returnPanel = panel;
			}
			else {
				panel.setFocus(false);
			}
		}
		return returnPanel;
	}

	private static Point getPoint(EventHandlingTarget drawPanelCanvas, HumanInputEvent<?> event) {
		Element e = drawPanelCanvas.getElement();
		if (event instanceof MouseEvent<?>) {
			return new Point(((MouseEvent<?>) event).getRelativeX(e), ((MouseEvent<?>) event).getRelativeY(e));
		}
		else if (event instanceof TouchEndEvent) {
			return new Point(((TouchEvent<?>) event).getChangedTouches().get(0).getRelativeX(e), ((TouchEvent<?>) event).getChangedTouches().get(0).getRelativeY(e));
		}
		else if (event instanceof TouchEvent<?>) {
			return new Point(((TouchEvent<?>) event).getTouches().get(0).getRelativeX(e), ((TouchEvent<?>) event).getTouches().get(0).getRelativeY(e));
		}
		else {
			throw new RuntimeException("Unknown Event Type: " + event);
		}
	}

	private static Point getPointAbsolute(HumanInputEvent<?> event) {
		if (event instanceof MouseEvent<?>) {
			return new Point(((MouseEvent<?>) event).getClientX(), ((MouseEvent<?>) event).getClientY());
		}
		else if (event instanceof TouchEndEvent) {
			return new Point(((TouchEvent<?>) event).getChangedTouches().get(0).getPageX(), ((TouchEvent<?>) event).getChangedTouches().get(0).getPageY());
		}
		else if (event instanceof TouchEvent<?>) {
			return new Point(((TouchEvent<?>) event).getTouches().get(0).getPageX(), ((TouchEvent<?>) event).getTouches().get(0).getPageY());
		}
		else {
			throw new RuntimeException("Unknown Event Type: " + event);
		}
	}
}
