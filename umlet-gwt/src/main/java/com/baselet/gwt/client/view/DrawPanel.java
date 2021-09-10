package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baselet.command.CommandTarget;
import com.baselet.control.SharedUtils;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.MenuConstants;
import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.Direction;
import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeChangeListener;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.element.GridElementUtils;
import com.baselet.element.Selector;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.sticking.StickableMap;
import com.baselet.gwt.client.base.Converter;
import com.baselet.gwt.client.base.NotificationPopup;
import com.baselet.gwt.client.base.Utils;
import com.baselet.gwt.client.clipboard.ClipboardShortcutWrapper;
import com.baselet.gwt.client.element.DiagramGwt;
import com.baselet.gwt.client.element.GridElementZoomUtil;
import com.baselet.gwt.client.file.FileChangeNotifier;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.view.EventHandlingUtils.EventHandlingTarget;
import com.baselet.gwt.client.view.interfaces.AutoresizeScrollDropTarget;
import com.baselet.gwt.client.view.interfaces.HasScrollPanel;
import com.baselet.gwt.client.view.widgets.MenuPopup;
import com.baselet.gwt.client.view.widgets.MenuPopup.MenuPopupItem;
import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;

import elemental2.dom.Element;
import jsinterop.base.Js;

public abstract class DrawPanel extends SimplePanel implements CommandTarget, HasMouseOutHandlers, HasMouseOverHandlers, EventHandlingTarget, AutoresizeScrollDropTarget, ThemeChangeListener {
	protected enum Zoom {
		IN, OUT, RESET
	}

	protected Diagram diagram = new DiagramGwt(new ArrayList<GridElement>());

	protected DrawCanvas canvas = new DrawCanvas();
	protected boolean cursorWasMovedDuringDrag; // check if cursor was actually moved

	SelectorNew selector;

	CommandInvoker commandInvoker = CommandInvoker.getInstance();

	ClipboardShortcutWrapper clipboardShortcutWrapper;

	DrawPanel otherDrawFocusPanel;

	HasScrollPanel scrollPanel;

	protected MainView mainView;

	PropertiesTextArea propertiesPanel;

	private MenuPopup elementContextMenu;
	private MenuPopup diagramContextMenu;
	private Point lastContextMenuPosition;

	protected Set<Direction> resizeDirections = new HashSet<Direction>();

	protected final Map<GridElement, StickableMap> stickablesToMove = new HashMap<GridElement, StickableMap>();

	public void setOtherDrawFocusPanel(DrawPanel otherDrawFocusPanel) {
		this.otherDrawFocusPanel = otherDrawFocusPanel;
	}

	private Boolean focus = false;

	protected FileChangeNotifier fileChangeNotifier;

	private NotificationPopup notificationPopup;

	private boolean mightNeedToCorrectVerticalPos = false;
	private boolean mightNeedToCorrectHorizontalPos = false;

	protected boolean insertingPreview = false;

	@Override
	public void setFocus(boolean focus) {
		if (this.focus == focus) {
			return;
		}
		if (focus) { // if focus has switched from diagram <-> palette, reset other selector and redraw
			otherDrawFocusPanel.getSelector().deselectAllWithoutAfterAction();
			otherDrawFocusPanel.redraw(); // redraw is necessary even if other afteractions (properties panel update) are not
			otherDrawFocusPanel.setFocus(false);
			EventHandlingUtils.getStorageInstance().setActivePanel(this);
		}
		this.focus = focus;
	}

	public Boolean getFocus() {
		return focus;
	}

	/* Changes the coordinates of GridElement e so that it is in the top left of targetPanel */
	public static void snapElementToVisibleTopLeft(GridElement e, DrawPanel targetPanel) {
		int gridSize = targetPanel.getDiagram().getZoomLevel();
		snapElementToVisibleTopLeft(e, targetPanel, gridSize, gridSize);
	}

	/* Changes the coordinates of GridElement e so that it is in the top left of targetPanel, with offset */
	public static void snapElementToVisibleTopLeft(GridElement e, DrawPanel targetPanel, int xOffset, int yOffset) {
		Rectangle visible = targetPanel.getVisibleBounds();
		e.setLocation(visible.getX() + xOffset, visible.getY() + yOffset);
	}

	/* Changes the coordinates of GridElement in the elementList so that they are in the top left of targetPanel */
	public static void snapElementsToVisibleTopLeft(List<GridElement> elementList, DrawPanel targetPanel) {
		int xOffset = targetPanel.getVisibleBounds().x + targetPanel.getAbsoluteLeft();
		int yOffset = targetPanel.getVisibleBounds().y + targetPanel.getAbsoluteTop();

		int gridSize = targetPanel.getDiagram().getZoomLevel();

		snapElementsToPointPosition(elementList, targetPanel, new Point(xOffset + gridSize, yOffset + gridSize));
	}

	/* Changes the coordinates of GridElement in the elementList so that they match a point point coordinates must be absolute, not in draw panel coordinates, but the method accounts for a scrolled drawPanel */
	public static void snapElementsToPointPosition(List<GridElement> elementList, DrawPanel targetPanel, Point point) {
		// ensure that at least one element is there since later code relies on that
		if (elementList.size() <= 0) {
			return;
		}

		// Get the the maximum X and Y values. it will act as the "top left pivot point" of the elements
		int pivotX = elementList.get(0).getRectangle().x;
		int pivotY = elementList.get(0).getRectangle().y;

		// find lowest x and y values, start at 1 since first values are already assigned
		for (int i = 1; i < elementList.size(); i++) {
			int xPosition = elementList.get(i).getRectangle().x;
			int yPosition = elementList.get(i).getRectangle().y;
			if (xPosition < pivotX) {
				pivotX = xPosition;
			}
			if (yPosition < pivotY) {
				pivotY = yPosition;
			}
		}

		int gridSize = targetPanel.getDiagram().getZoomLevel();
		// Snap paste position to grid
		int remX = point.getX() % gridSize;
		int remY = point.getY() % gridSize;
		point.setX(remX >= gridSize / 2 ? point.getX() - remX + gridSize : point.getX() - remX);
		point.setY(remY >= gridSize / 2 ? point.getY() - remY + gridSize : point.getY() - remY);

		// snap all elements to point, but relative to the pivot
		for (GridElement e : elementList) {
			int xOffset = e.getRectangle().x - pivotX;
			int yOffset = e.getRectangle().y - pivotY;
			Rectangle visible = targetPanel.getVisibleBounds();
			e.setLocation(xOffset + point.getX() - targetPanel.getAbsoluteLeft(), yOffset + point.getY() - targetPanel.getAbsoluteTop());
		}
	}

	public DrawPanel() {}

	public void init(final MainView mainView, final PropertiesTextArea propertiesPanel) {
		this.setStylePrimaryName("canvasFocusPanel");

		this.mainView = mainView;
		this.propertiesPanel = propertiesPanel;

		selector = new SelectorNew(diagram) {
			@Override
			public void doAfterSelectionChanged() {
				updatePropertiesPanelWithSelectedElement();
			}
		};

		createMenuPopups();

		this.add(canvas.getWidget());

		clipboardShortcutWrapper = GWT.create(ClipboardShortcutWrapper.class);
		fileChangeNotifier = GWT.create(FileChangeNotifier.class);

		ThemeFactory.addListener(this);
		notificationPopup = new NotificationPopup();
	}

	protected List<MenuPopupItem> getStandardAdditionalElementMenuItems() {
		return Arrays.asList(
				new MenuPopupItem(MenuConstants.GROUP) {
					@Override
					public void execute() {
						String unusedGroup = selector.getUnusedGroup();
						commandInvoker.updateSelectedElementsProperty(DrawPanel.this, GroupFacet.KEY, unusedGroup);
					}
				}, new MenuPopupItem(MenuConstants.UNGROUP) {
					@Override
					public void execute() {
						commandInvoker.updateSelectedElementsProperty(DrawPanel.this, GroupFacet.KEY, null);
					}
				});
	}

	protected List<MenuPopupItem> getStandardMenuPopupItems() {
		return Arrays.asList(
				new MenuPopupItem(MenuConstants.DELETE) {
					@Override
					public void execute() {
						commandInvoker.removeSelectedElements(DrawPanel.this);
					}
				}, new MenuPopupItem(MenuConstants.COPY) {
					@Override
					public void execute() {
						commandInvoker.copySelectedElements(DrawPanel.this);
					}
				}, new MenuPopupItem(MenuConstants.CUT) {
					@Override
					public void execute() {
						commandInvoker.cutSelectedElements(DrawPanel.this);
					}
				}, new MenuPopupItem(MenuConstants.PASTE) {
					@Override
					public void execute() {
						commandInvoker.pasteElements();
					}
				}, new MenuPopupItem(MenuConstants.SELECT_ALL) {
					@Override
					public void execute() {
						selector.select(diagram.getGridElements());
					}
				});
	}

	@Override
	public void updatePropertiesPanelWithSelectedElement() {
		List<GridElement> elements = selector.getSelectedElements();
		if (!elements.isEmpty()) { // always set properties text of latest selected element (so you also have an element in the prop panel even if you have an active multiselect)
			propertiesPanel.setGridElement(elements.get(elements.size() - 1), DrawPanel.this);
		}
		else {
			propertiesPanel.setGridElement(diagram, DrawPanel.this);
		}
		redraw(!insertingPreview);
	}

	void keyboardMoveSelectedElements(int diffX, int diffY) {
		List<GridElement> gridElements = selector.getSelectedElements();
		moveElements(diffX, diffY, true, gridElements);
		dragEndAndRedraw(gridElements);

		// redraw(true, true);
	}

	public void setGridAndZoom(int factor, boolean manualZoom, Point position) {
		Diagram diagram = getDiagram();
		int oldGridSize = diagram.getZoomLevel();

		if (factor < 1 || factor > 20) {
			return; // Only zoom between 10% and 200% is allowed
		}
		if (factor == oldGridSize) {
			return; // Only zoom if gridsize has changed
		}

		diagram.setZoomLevel(factor);

		/**
		 * Zoom entities to the new gridsize
		 */
		GridElementZoomUtil.zoomEntities(oldGridSize, factor, getDiagram().getGridElements());

		// Zoom to mouse position if available
		if (manualZoom) {
			float x = position.x;
			float y = position.y;

			float diffx, diffy;
			diffx = x - x * factor / oldGridSize;
			diffy = y - y * factor / oldGridSize;

			for (GridElement e : getDiagram().getGridElements()) {
				e.setLocationDifference(realignToGrid(diffx), realignToGrid(diffy));
			}
			notificationPopup.hide();
			notificationPopup.show("Diagram zoomed to " + factor * 10 + "%", this);
		}
		redraw(true);
	}

	protected void realignElementsToGrid(List<GridElement> elements) {
		for (GridElement element : elements) {
			element.setLocation(realignToGrid(element.getRectangle().getX()), realignToGrid(element.getRectangle().getY()));
		}
	}

	protected int realignToGrid(float diff) {
		return SharedUtils.realignTo(false, diff, false, getDiagram().getZoomLevel());
	}

	void moveElements(int diffX, int diffY, boolean firstDrag, List<GridElement> elements) {
		if (elements.isEmpty()) { // if nothing is selected, move whole diagram
			elements = diagram.getGridElements();
		}
		for (GridElement ge : elements) {
			if (firstDrag) {
				stickablesToMove.put(ge, getStickablesToMoveWhenElementsMove(ge, elements));
			}
			ge.setRectangleDifference(diffX, diffY, 0, 0, firstDrag, stickablesToMove.get(ge), false); // uses setLocationDifference() instead of drag() to avoid special handling (eg: from Relations)
		}
	}

	public Point getLastContextMenuPosition() {
		if (elementContextMenu.isShowing() || diagramContextMenu.isShowing()) {
			return lastContextMenuPosition;
		}
		else {
			return null;
		}
	}

	@Override
	public Rectangle getVisibleBounds() {
		return scrollPanel.getVisibleBounds();
	}

	@Override
	public void redraw() {
		redraw(true);
	}

	public void redraw(boolean recalcSize) {
		List<GridElement> gridElements = diagram.getGridElementsByLayerLowestToHighest();
		if (recalcSize) {
			if (scrollPanel == null) {
				return;
			}
			Element canvasElement = Js.cast(canvas.getCanvasElement());
			int[] canvasBoundedRectCoordinates = new int[] { (int) canvasElement.getBoundingClientRect().x, (int) canvasElement.getBoundingClientRect().y };
			int[] panelBoundedRectCoordinates = new int[] { (int) scrollPanel.getBoundedRectCoordinates().x, (int) scrollPanel.getBoundedRectCoordinates().y };

			Rectangle diagramRect = GridElementUtils.getGridElementsRectangle(gridElements);
			Rectangle visibleRect = getVisibleBounds();
			// realign top left corner of the diagram back to the canvas and remove invisible whitespace outside of the diagram
			final int xTranslate = Math.min(visibleRect.getX(), diagramRect.getX()); // can be positive (to cut upper left whitespace without diagram) or negative (to move diagram back to the visible canvas which starts at (0,0))
			final int yTranslate = Math.min(visibleRect.getY(), diagramRect.getY());
			if (xTranslate != 0 || yTranslate != 0) {
				// temp increase of canvas size to make sure scrollbar can be moved
				canvas.clearAndSetSize(canvas.getWidth() + Math.abs(xTranslate), canvas.getHeight() + Math.abs(yTranslate));
				// move scrollbars
				scrollPanel.moveHorizontalScrollbar(-xTranslate);
				scrollPanel.moveVerticalScrollbar(-yTranslate);
				// then move gridelements to correct position
				for (GridElement ge : gridElements) {
					ge.setLocationDifference(-xTranslate, -yTranslate);
				}
			}
			// now realign bottom right corner to include the translate-factor and the changed visible and diagram rect
			int width = Math.max(visibleRect.getX2(), diagramRect.getX2()) - xTranslate;
			int height = Math.max(visibleRect.getY2(), diagramRect.getY2()) - yTranslate;

			// depending on sizes of canvas and panel reduce canvas size by scrollbar dimensions. If x or y position of
			// canvas becomes positive again and scrollbar is going to disappear move elements and redraw to compensate
			// for canvas move due to increased panel size because of now missing scrollbar
			int canvasHeight = height;
			int canvasWidth = width;
			if (canvasBoundedRectCoordinates[1] >= panelBoundedRectCoordinates[1]) {
				mightNeedToCorrectVerticalPos = false;
			}
			if (canvasBoundedRectCoordinates[0] >= panelBoundedRectCoordinates[0]) {
				mightNeedToCorrectHorizontalPos = false;
			}
			if (width <= visibleRect.getWidth() && height <= visibleRect.getHeight()) {
				mightNeedToCorrectVerticalPos = false;
				mightNeedToCorrectHorizontalPos = false;
			}

			if (width > visibleRect.getWidth()) {
				canvasHeight -= scrollPanel.getScrollbarSize()[1];
				if (canvasBoundedRectCoordinates[1] < panelBoundedRectCoordinates[1]) {
					mightNeedToCorrectVerticalPos = true;
				}
			}
			else if (mightNeedToCorrectVerticalPos) {
				mightNeedToCorrectVerticalPos = false;
				if (!cursorWasMovedDuringDrag && (scrollPanel.getVerticalScrollPosition() == 0 || scrollPanel.getVerticalScrollPosition() == scrollPanel.getMaximumVerticalScrollPosition())) {
					for (GridElement ge : gridElements) {
						ge.setLocationDifference(0, -scrollPanel.getScrollbarSize()[1]);
					}
				}
				redraw();
				return;
			}

			if (height > visibleRect.getHeight()) {
				canvasWidth -= scrollPanel.getScrollbarSize()[0];
				if (canvasBoundedRectCoordinates[0] < panelBoundedRectCoordinates[0]) {
					mightNeedToCorrectHorizontalPos = true;
				}
			}
			else if (mightNeedToCorrectHorizontalPos) {
				mightNeedToCorrectHorizontalPos = false;
				if (!cursorWasMovedDuringDrag && (scrollPanel.getHorizontalScrollPosition() == 0 || scrollPanel.getHorizontalScrollPosition() == scrollPanel.getMaximumHorizontalScrollPosition())) {
					for (GridElement ge : gridElements) {
						ge.setLocationDifference(-scrollPanel.getScrollbarSize()[0], 0);
					}
				}
				redraw();
				return;
			}

			canvas.clearAndSetSize(canvasWidth, canvasHeight);
		}
		else {
			canvas.clearAndSetSize(canvas.getWidth(), canvas.getHeight());
		}
		canvas.draw(true, gridElements, selector);
	}

	@Override
	public GridElement getGridElementOnPosition(Point point) {
		GridElement returnGe = null;
		for (GridElement ge : diagram.getGridElementsByLayer(false)) { // get elements, highest layer first
			if (returnGe != null && returnGe.getLayer() > ge.getLayer()) {
				break; // because the following elements have lower layers, break if a valid higher layered element has been found
			}
			if (ge.isSelectableOn(point)) {
				if (returnGe == null) {
					returnGe = ge;
				}
				else {
					boolean newIsSelectedOldNot = selector.isSelected(ge) && !selector.isSelected(returnGe);
					boolean oldContainsNew = returnGe.getRectangle().contains(ge.getRectangle());
					if (newIsSelectedOldNot || oldContainsNew) {
						returnGe = ge;
					}
				}
			}
		}
		return returnGe;
	}

	@Override
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
		selector.setGridElementProvider(diagram);
		selector.deselectAll(); // necessary to trigger setting helptext to properties
		redraw(true);
	}

	@Override
	public void addGridElements(List<GridElement> elements) {
		addGridElements(elements, true);
	}

	@Override
	public void addGridElements(List<GridElement> elements, boolean recalcSize) {
		diagram.getGridElements().addAll(elements);
		realignElementsToGrid(diagram.getGridElements());
		selector.selectOnly(elements);
		redraw(recalcSize);
	}

	@Override
	public void removeGridElements(List<GridElement> elements) {
		diagram.getGridElements().removeAll(elements);
		selector.deselect(elements);
	}

	@Override
	public Diagram getDiagram() {
		return diagram;
	}

	@Override
	public Selector getSelector() {
		return selector;
	}

	@Override
	public void setAutoresizeScrollDrop(HasScrollPanel scrollPanel) {
		this.scrollPanel = scrollPanel;
	}

	@Override
	public abstract void onDoubleClick(GridElement ge);

	@Override
	public void onMouseDragEnd(GridElement gridElement, Point lastPoint) {
		dragEndAndRedraw(selector.getSelectedElements());
	}

	private void dragEndAndRedraw(List<GridElement> selectedElements) {
		for (GridElement ge : selectedElements) {
			stickablesToMove.remove(ge);
			ge.dragEnd();
		}
		if (selector.isLassoActive()) {
			selector.selectElementsInsideLasso(getDiagram().getGridElements());
		}
		redraw(true);
	}

	@Override
	public void onMouseDownScheduleDeferred(final GridElement element, final boolean isControlKeyDown) {
		Scheduler.get().scheduleFinally(new ScheduledCommand() { // scheduleDeferred is necessary for mobile (or low performance) browsers
			@Override
			public void execute() {
				onMouseDown(element, isControlKeyDown);
			}
		});

	}

	void onMouseDown(final GridElement element, final boolean isControlKeyDown) {
		cursorWasMovedDuringDrag = false;
		if (isControlKeyDown) {
			if (element != null) {
				if (selector.isSelected(element)) {
					selector.deselect(element);
				}
				else {
					selector.select(element);
				}
			}
		}
		else {
			if (element != null) {
				if (selector.isSelected(element)) {
					selector.moveToLastPosInList(element);
					propertiesPanel.setGridElement(element, DrawPanel.this);
				}
				else {
					selector.selectOnly(element);
				}
			}
			else {
				selector.deselectAll();
			}
		}
	}

	@Override
	public void onMouseMoveDraggingScheduleDeferred(final Point dragStart, final int diffX, final int diffY, final GridElement draggedGridElement, final boolean isShiftKeyDown, final boolean isCtrlKeyDown, final boolean firstDrag, final boolean isMiddleMouseButton) {
		Scheduler.get().scheduleFinally(new ScheduledCommand() { // scheduleDeferred is necessary for mobile (or low performance) browsers
			@Override
			public void execute() {
				onMouseMoveDragging(dragStart, diffX, diffY, draggedGridElement, isShiftKeyDown, isCtrlKeyDown, firstDrag, isMiddleMouseButton);
			}
		});
	}

	void onMouseMoveDragging(Point dragStart, int diffX, int diffY, GridElement draggedGridElement, boolean isShiftKeyDown, boolean isCtrlKeyDown, boolean firstDrag, boolean isMiddleMouseButton) {
		if (diffX != 0 || diffY != 0) {
			cursorWasMovedDuringDrag = true;
		}
		if (firstDrag && draggedGridElement != null && !isMiddleMouseButton) { // if draggedGridElement == null the whole diagram is dragged and nothing has to be checked for sticking
			stickablesToMove.put(draggedGridElement, getStickablesToMoveWhenElementsMove(draggedGridElement, Collections.<GridElement> emptyList()));
		}
		if (isCtrlKeyDown && !selector.isLassoActive() && !isMiddleMouseButton) {
			selector.startSelection(dragStart);
		}
		if (selector.isLassoActive()) {
			selector.updateLasso(diffX, diffY);
		}
		else if (isMiddleMouseButton) {
			moveElements(diffX, diffY, firstDrag, new ArrayList<>());
		}
		else if (!resizeDirections.isEmpty()) {
			draggedGridElement.drag(resizeDirections, diffX, diffY, getRelativePoint(dragStart, draggedGridElement), isShiftKeyDown, firstDrag, stickablesToMove.get(draggedGridElement), false);
		}
		// if a single element is selected, drag it (and pass the dragStart, because it's important for Relations)
		else if (selector.getSelectedElements().size() == 1) {
			draggedGridElement.drag(Collections.<Direction> emptySet(), diffX, diffY, getRelativePoint(dragStart, draggedGridElement), isShiftKeyDown, firstDrag, stickablesToMove.get(draggedGridElement), false);
		}
		else if (!selector.isLassoActive()) { // if != 1 elements are selected, move them
			moveElements(diffX, diffY, firstDrag, selector.getSelectedElements());
		}
		redraw(false);
	}

	protected Point getRelativePoint(Point dragStart, GridElement draggedGridElement) {
		return new Point(dragStart.getX() - draggedGridElement.getRectangle().getX(), dragStart.getY() - draggedGridElement.getRectangle().getY());
	}

	protected StickableMap getStickablesToMoveWhenElementsMove(GridElement draggedElement, List<GridElement> excludeList) {
		return diagram.getStickables(draggedElement, excludeList);
	}

	@Override
	public void onMouseMove(Point absolute) {
		GridElement geOnPosition = getGridElementOnPosition(absolute);
		if (geOnPosition != null) { // exactly one gridelement selected which is at the mouseposition
			resizeDirections = geOnPosition.getResizeArea(absolute.getX() - geOnPosition.getRectangle().getX(), absolute.getY() - geOnPosition.getRectangle().getY());
			CursorOwn cursor = geOnPosition.getCursor(absolute, resizeDirections);
			if (cursor != null) {
				Utils.showCursor(cursor);
			}
		}
		else {
			resizeDirections.clear();
			Utils.showCursor(CursorOwn.DEFAULT);
		}
	}

	@Override
	public void onShowMenu(Point point) {
		Point relativePoint = new Point(point.x - getAbsoluteLeft(), point.y - getAbsoluteTop());
		if (getGridElementOnPosition(relativePoint) == null) { // gridelement check must be made with relative coordinates
			diagramContextMenu.show(point);
		}
		else {
			elementContextMenu.show(point);
		}
		lastContextMenuPosition = point; // used to determine paste positions
	}

	@Override
	public void handleKeyDown(KeyDownEvent event) {
		boolean avoidBrowserDefault = true;
		if (Shortcut.DELETE_ELEMENT.matches(event)) {
			commandInvoker.removeSelectedElements(DrawPanel.this);
		}
		else if (Shortcut.DESELECT_ALL.matches(event)) {
			selector.deselectAll();
		}
		else if (Shortcut.SELECT_ALL.matches(event)) {
			selector.select(diagram.getGridElements());
			event.stopPropagation();
		}
		else if (Shortcut.COPY.matches(event)) {
			clipboardShortcutWrapper.onCopy(DrawPanel.this);
		}
		else if (Shortcut.CUT.matches(event)) {
			clipboardShortcutWrapper.onCut(DrawPanel.this);
		}
		else if (Shortcut.PASTE.matches(event)) {
			clipboardShortcutWrapper.onPaste();
		}
		else if (Shortcut.SAVE.matches(event)) {
			mainView.getSaveCommand().execute();
		}
		else if (Shortcut.MOVE_UP.matches(event)) {
			keyboardMoveSelectedElements(0, -diagram.getZoomLevel());
		}
		else if (Shortcut.MOVE_DOWN.matches(event)) {
			keyboardMoveSelectedElements(0, diagram.getZoomLevel());
		}
		else if (Shortcut.MOVE_LEFT.matches(event)) {
			keyboardMoveSelectedElements(-diagram.getZoomLevel(), 0);
		}
		else if (Shortcut.MOVE_RIGHT.matches(event)) {
			keyboardMoveSelectedElements(diagram.getZoomLevel(), 0);
		}
		else if (Shortcut.DISABLE_STICKING.matches(event)) {
			SharedConfig.getInstance().setStickingEnabled(false);
		}
		else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
			// Don't do anything on TAB key down
		}
		else if (Shortcut.ZOOM_IN.matches(event)) {
			zoom(Zoom.IN);
		}
		else if (Shortcut.ZOOM_OUT.matches(event)) {
			zoom(Zoom.OUT);
		}
		else if (Shortcut.ZOOM_RESET.matches(event)) {
			zoom(Zoom.RESET);
		}
		else {
			avoidBrowserDefault = false;
		}

		// avoid browser default key handling for all overwritten keys, but not for others (like F5 for refresh or the zoom controls)
		if (avoidBrowserDefault) {
			event.preventDefault();
		}
	}

	protected void zoom(String zoomValue) {
		try {
			zoom(Zoom.valueOf(zoomValue), new Point());
		} catch (IllegalArgumentException e) {
			CustomLoggerFactory.getLogger(DrawPanel.class).error("zoom() called with non-existing Zoom enum value!");
		}
	}

	protected void zoom(Zoom zoom) {
		zoom(zoom, new Point());
	}

	protected void zoom(Zoom zoom, Point point) {
		if (focus) {
			switch (zoom) {
				case IN:
					setGridAndZoom(getDiagram().getZoomLevel() + 1, true, point);
					break;
				case OUT:
					setGridAndZoom(getDiagram().getZoomLevel() - 1, true, point);
					break;
				case RESET:
					setGridAndZoom(SharedConstants.DEFAULT_GRID_SIZE, true, point);
					break;
			}
		}
	}

	@Override
	public void onMouseWheelZoom(MouseWheelEvent event) {
		if (event.isControlKeyDown()) {
			event.stopPropagation();
			event.preventDefault();
			Point zoomPoint = new Point(event.getX(), event.getY());
			zoom(event.isNorth() ? Zoom.IN : Zoom.OUT, zoomPoint);
		}
	}

	public static native void clearSelection() /*-{
		if (window.getSelection) {
			window.getSelection().removeAllRanges();
		} else if (document.selection) {
			document.selection.empty();
		}
	}-*/;

	@Override
	public void handleKeyUp(KeyUpEvent event) {
		if (Shortcut.DISABLE_STICKING.matches(event)) {
			SharedConfig.getInstance().setStickingEnabled(true);
		}
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public void onThemeChange() {
		updateGridElements();
		redraw(false);

		elementContextMenu.getElement().getStyle().setColor(Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_FOREGROUND)).value());
		diagramContextMenu.getElement().getStyle().setColor(Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_FOREGROUND)).value());
		createMenuPopups();
	}

	public void updateGridElements() {
		List<GridElement> gridElements = diagram.getGridElements();
		for (GridElement gridElement : gridElements) {
			gridElement.updateModelFromText();
		}
	}

	private void createMenuPopups() {
		List<MenuPopupItem> diagramItems = getStandardMenuPopupItems();
		List<MenuPopupItem> elementItems = new ArrayList<MenuPopupItem>(diagramItems);
		elementItems.addAll(getStandardAdditionalElementMenuItems());
		diagramContextMenu = new MenuPopup(diagramItems);
		elementContextMenu = new MenuPopup(elementItems);
	}

	@Override
	public int getGridSize() {
		return diagram.getZoomLevel();
	}
}
