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
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.MenuConstants;
import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.Direction;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.diagram.draw.helper.theme.ThemeChangeListener;
import com.baselet.element.GridElementUtils;
import com.baselet.element.Selector;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.sticking.StickableMap;
import com.baselet.gwt.client.base.Utils;
import com.baselet.gwt.client.element.DiagramGwt;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.view.EventHandlingUtils.EventHandlingTarget;
import com.baselet.gwt.client.view.interfaces.AutoresizeScrollDropTarget;
import com.baselet.gwt.client.view.interfaces.HasScrollPanel;
import com.baselet.gwt.client.view.widgets.MenuPopup;
import com.baselet.gwt.client.view.widgets.MenuPopup.MenuPopupItem;
import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class DrawPanel extends SimplePanel implements CommandTarget, HasMouseOutHandlers, HasMouseOverHandlers, EventHandlingTarget, AutoresizeScrollDropTarget, ThemeChangeListener {

	protected Diagram diagram = new DiagramGwt(new ArrayList<GridElement>());

	protected DrawCanvas canvas = new DrawCanvas();
	protected boolean cursorWasMovedDuringDrag; //check if cursor was actually moved

	SelectorNew selector;

	CommandInvoker commandInvoker = CommandInvoker.getInstance();

	DrawPanel otherDrawFocusPanel;

	HasScrollPanel scrollPanel;

	protected final MainView mainView;

	PropertiesTextArea propertiesPanel;

	private final MenuPopup elementContextMenu;
	private final MenuPopup diagramContextMenu;

	protected Set<Direction> resizeDirections = new HashSet<Direction>();

	protected final Map<GridElement, StickableMap> stickablesToMove = new HashMap<GridElement, StickableMap>();

	public void setOtherDrawFocusPanel(DrawPanel otherDrawFocusPanel) {
		this.otherDrawFocusPanel = otherDrawFocusPanel;
	}

	private Boolean focus = false;

	@Override
	public void setFocus(boolean focus) {
		if (this.focus == focus) {
			return;
		}
		if (focus) { // if focus has switched from diagram <-> palette, reset other selector and redraw
			otherDrawFocusPanel.getSelector().deselectAllWithoutAfterAction();
			otherDrawFocusPanel.redraw(); // redraw is necessary even if other afteractions (properties panel update) are not
			otherDrawFocusPanel.setFocus(false);
		}
		this.focus = focus;
	}

	public Boolean getFocus() {
		return focus;
	}


	public DrawPanel(final MainView mainView, final PropertiesTextArea propertiesPanel) {
		this.setStylePrimaryName("canvasFocusPanel");

		this.mainView = mainView;
		this.propertiesPanel = propertiesPanel;

		selector = new SelectorNew(diagram) {
			@Override
			public void doAfterSelectionChanged() {
				updatePropertiesPanelWithSelectedElement();
			}
		};

		List<MenuPopupItem> diagramItems = getStandardMenuPopupItems();
		List<MenuPopupItem> elementItems = new ArrayList<MenuPopupItem>(diagramItems);
		elementItems.addAll(getStandardAdditionalElementMenuItems());
		diagramContextMenu = new MenuPopup(diagramItems);
		elementContextMenu = new MenuPopup(elementItems);

		this.add(canvas.getWidget());
		ThemeFactory.addListener(this);
	}


	protected List<MenuPopupItem> getStandardAdditionalElementMenuItems() {
		return Arrays.asList(
				new MenuPopupItem(MenuConstants.GROUP) {
					@Override
					public void execute() {
						Integer unusedGroup = selector.getUnusedGroup();
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
							commandInvoker.pasteElements(DrawPanel.this);
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
		redraw();
	}

	void keyboardMoveSelectedElements(int diffX, int diffY) {
		List<GridElement> gridElements = selector.getSelectedElements();
		moveElements(diffX, diffY, true, gridElements);
		dragEndAndRedraw(gridElements);
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

	@Override
	public Rectangle getVisibleBounds() {
		return scrollPanel.getVisibleBounds();
	}

	@Override
	public void redraw() {
		redraw(true);
	}

	void redraw(boolean recalcSize) {
		List<GridElement> gridElements = diagram.getGridElementsByLayerLowestToHighest();
		if (recalcSize) {
			if (scrollPanel == null) {
				return;
			}

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
			canvas.clearAndSetSize(width, height);
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
		redraw();
	}

	@Override
	public void addGridElements(List<GridElement> elements) {
		diagram.getGridElements().addAll(elements);
		selector.selectOnly(elements);
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
		redraw();
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
	public void onMouseMoveDraggingScheduleDeferred(final Point dragStart, final int diffX, final int diffY, final GridElement draggedGridElement, final boolean isShiftKeyDown, final boolean isCtrlKeyDown, final boolean firstDrag) {
		Scheduler.get().scheduleFinally(new ScheduledCommand() { // scheduleDeferred is necessary for mobile (or low performance) browsers
			@Override
			public void execute() {
				onMouseMoveDragging(dragStart, diffX, diffY, draggedGridElement, isShiftKeyDown, isCtrlKeyDown, firstDrag);
			}
		});
	}

	void onMouseMoveDragging(Point dragStart, int diffX, int diffY, GridElement draggedGridElement, boolean isShiftKeyDown, boolean isCtrlKeyDown, boolean firstDrag) {
		if (diffX != 0 || diffY != 0)
		{
			cursorWasMovedDuringDrag = true;
		}
		if (firstDrag && draggedGridElement != null) { // if draggedGridElement == null the whole diagram is dragged and nothing has to be checked for sticking
			stickablesToMove.put(draggedGridElement, getStickablesToMoveWhenElementsMove(draggedGridElement, Collections.<GridElement> emptyList()));
		}
		if (isCtrlKeyDown) {
			return; // TODO implement Lasso
		}
		else if (!resizeDirections.isEmpty()) {
			draggedGridElement.drag(resizeDirections, diffX, diffY, getRelativePoint(dragStart, draggedGridElement), isShiftKeyDown, firstDrag, stickablesToMove.get(draggedGridElement), false);
		}
		// if a single element is selected, drag it (and pass the dragStart, because it's important for Relations)
		else if (selector.getSelectedElements().size() == 1) {
			draggedGridElement.drag(Collections.<Direction> emptySet(), diffX, diffY, getRelativePoint(dragStart, draggedGridElement), isShiftKeyDown, firstDrag, stickablesToMove.get(draggedGridElement), false);
		}
		else { // if != 1 elements are selected, move them
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
		}
		else if (Shortcut.COPY.matches(event)) {
			commandInvoker.copySelectedElements(DrawPanel.this);
		}
		else if (Shortcut.CUT.matches(event)) {
			commandInvoker.cutSelectedElements(DrawPanel.this);
		}
		else if (Shortcut.PASTE.matches(event)) {
			commandInvoker.pasteElements(DrawPanel.this);
		}
		else if (Shortcut.SAVE.matches(event) && !VersionChecker.isVsCodeVersion()) {
			mainView.getSaveCommand().execute();
		}
		else if (Shortcut.MOVE_UP.matches(event)) {
			keyboardMoveSelectedElements(0, -SharedConstants.DEFAULT_GRID_SIZE);
			redraw();
		}
		else if (Shortcut.MOVE_DOWN.matches(event)) {
			keyboardMoveSelectedElements(0, SharedConstants.DEFAULT_GRID_SIZE);
			redraw();
		}
		else if (Shortcut.MOVE_LEFT.matches(event)) {
			keyboardMoveSelectedElements(-SharedConstants.DEFAULT_GRID_SIZE, 0);
			redraw();
		}
		else if (Shortcut.MOVE_RIGHT.matches(event)) {
			keyboardMoveSelectedElements(SharedConstants.DEFAULT_GRID_SIZE, 0);
			redraw();
		}
		else if (Shortcut.DISABLE_STICKING.matches(event)) {
			SharedConfig.getInstance().setStickingEnabled(false);
		}
		else {
			avoidBrowserDefault = false;
		}

		// avoid browser default key handling for all overwritten keys, but not for others (like F5 for refresh or the zoom controls)
		if (avoidBrowserDefault) {
			event.preventDefault();
		}
	}

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
		List<GridElement> gridElements = diagram.getGridElements();
		for(GridElement gridElement:gridElements) {
			gridElement.updateModelFromText();
		}
		redraw(false);
	}
}
