package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.baselet.control.MenuConstants;
import com.baselet.control.SharedConstants;
import com.baselet.control.SharedUtils;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.commandnew.CanAddAndRemoveGridElement;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.gwt.client.Utils;
import com.baselet.gwt.client.element.Diagram;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.view.widgets.MenuPopup;
import com.baselet.gwt.client.view.widgets.MenuPopup.MenuPopupItem;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class DrawPanel extends SimplePanel implements CanAddAndRemoveGridElement, HasMouseOutHandlers, HasMouseOverHandlers {

	private static final Logger log = Logger.getLogger(DrawPanel.class);

	private Diagram diagram = new Diagram(new ArrayList<GridElement>());

	protected DrawCanvas canvas = new DrawCanvas();

	SelectorNew selector;

	CommandInvoker commandInvoker = CommandInvoker.getInstance();

	DrawPanel otherDrawFocusPanel;

	AutoResizeScrollDropPanel scrollPanel;

	private MainView mainView;

	private PropertiesTextArea propertiesPanel;

	private MenuPopup elementContextMenu;
	private MenuPopup diagramContextMenu;

	public void setOtherDrawFocusPanel(DrawPanel otherDrawFocusPanel) {
		this.otherDrawFocusPanel = otherDrawFocusPanel;
	}

	private Boolean focus = false;

	public void setFocus(boolean focus) {
		if (this.focus == focus) return;
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
			public void doAfterSelectionChanged() {
				updatePropertiesPanelWithSelectedElement();
			}
		};

		List<MenuPopupItem> diagramItems = Arrays.asList(
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
		List<MenuPopupItem> elementItems = new ArrayList<MenuPopupItem>(diagramItems);
		elementItems.addAll(Arrays.asList(
				new MenuPopupItem(MenuConstants.GROUP) {
					@Override
					public void execute() {
						commandInvoker.updateSelectedElementsGroup(DrawPanel.this, true);
					}
				}, new MenuPopupItem(MenuConstants.UNGROUP) {
					@Override
					public void execute() {
						commandInvoker.updateSelectedElementsGroup(DrawPanel.this, false);
					}
				}));
		diagramContextMenu = new MenuPopup(diagramItems);
		elementContextMenu = new MenuPopup(elementItems);

		this.add(canvas.getWidget());
	}

	public void updatePropertiesPanelWithSelectedElement() {
		List<GridElement> elements = selector.getSelectedElements();
		if (!elements.isEmpty()) { // always set properties text of latest selected element (so you also have an element in the prop panel even if you have an active multiselect)
			propertiesPanel.setGridElement(elements.get(elements.size()-1), DrawPanel.this);
		} else {
			propertiesPanel.setGridElement(diagram, DrawPanel.this);
		}
		redraw();
	}

	void moveSelectedElements(int diffX, int diffY, boolean firstDrag) {
		List<GridElement> elements = selector.getSelectedElements();
		if (elements.isEmpty()) { // if nothing is selected, move whole diagram
			elements = diagram.getGridElements();
		}
		diagram.moveGridElements(diffX, diffY, firstDrag, elements);
	}

	Rectangle getVisibleBounds() {
		return scrollPanel.getVisibleBounds();
	}

	public void redraw() {
		redraw(true);
	}

	void redraw(boolean recalcSize) {
		List<GridElement> gridElements = diagram.getGridElementsSortedByLayer();
		if (recalcSize) {
			if (scrollPanel == null) return;

			Rectangle diagramRect = SharedUtils.getGridElementsRectangle(gridElements);
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
			int width = Math.max(visibleRect.getX2(), diagramRect.getX2())-xTranslate;
			int height = Math.max(visibleRect.getY2(), diagramRect.getY2())-yTranslate;
			canvas.clearAndSetSize(width, height);
		} else {
			canvas.clearAndSetSize(canvas.getWidth(), canvas.getHeight());
		}
		canvas.draw(true, gridElements, selector);
	}

	public GridElement getGridElementOnPosition(Point point) {
		GridElement returnGe = null;
		returnGe = getGridElementOnPositionHelper(point, diagram.getRelations());
		if (returnGe == null) { // if no relation, search all elements
			returnGe = getGridElementOnPositionHelper(point, diagram.getGridElements());
		}
		return returnGe;
	}

	private GridElement getGridElementOnPositionHelper(Point point, Collection<? extends GridElement> elements) {
		GridElement returnGe = null;
		for (GridElement ge : elements) {
			if (ge.isSelectableOn(point)) {
				if (returnGe == null) {
					returnGe = ge;
				} else {
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


	public Diagram getDiagram() {
		return diagram;
	}

	public Selector getSelector() {
		return selector;
	}

	public void setScrollPanel(AutoResizeScrollDropPanel scrollPanel) {
		this.scrollPanel = scrollPanel;
	}

	public abstract void onDoubleClick(GridElement ge);

	public void onMouseDragEnd(GridElement gridElement, Point lastPoint) {
		for (GridElement ge : selector.getSelectedElements()) {
			ge.dragEnd();
		}
		redraw();
	}

	public void onMouseDownScheduleDeferred(final GridElement element, final boolean isControlKeyDown) {
		Scheduler.get().scheduleFinally(new ScheduledCommand() { // scheduleDeferred is necessary for mobile (or low performance) browsers
			@Override
			public void execute() {
				onMouseDown(element, isControlKeyDown);
			}
		});
	}

	void onMouseDown(final GridElement element, final boolean isControlKeyDown) {
		if (isControlKeyDown) {
			if (element != null) {
				if (selector.isSelected(element)) {
					selector.deselect(element);
				} else {
					selector.select(element);
				}
			}
		} else {
			if (element != null) {
				if (selector.isSelected(element)) {
					selector.moveToLastPosInList(element);
					propertiesPanel.setGridElement(element, DrawPanel.this);
				} else {
					selector.selectOnly(element);
				}
			} else {
				selector.deselectAll();
			}
		}
	}

	private Set<Direction> resizeDirection = new HashSet<Direction>();

	public void onMouseMoveDraggingScheduleDeferred(final Point dragStart, final int diffX, final int diffY, final GridElement draggedGridElement, final boolean isShiftKeyDown, final boolean isCtrlKeyDown, final boolean firstDrag) {
		Scheduler.get().scheduleFinally(new ScheduledCommand() { // scheduleDeferred is necessary for mobile (or low performance) browsers
			@Override
			public void execute() {
				onMouseMoveDragging(dragStart, diffX, diffY, draggedGridElement, isShiftKeyDown, isCtrlKeyDown, firstDrag);
			}
		});
	}

	void onMouseMoveDragging(Point dragStart, int diffX, int diffY, GridElement draggedGridElement, boolean isShiftKeyDown, boolean isCtrlKeyDown, boolean firstDrag) {
		if (isCtrlKeyDown) {
			return; // TODO implement Lasso
		}
		// if cursorpos determines a resizedirection, resize the element from where the mouse is dragging (eg: if 2 elements are selected, you can resize any of them without losing your selection)
		else if (!resizeDirection.isEmpty()) {
			draggedGridElement.drag(resizeDirection, diffX, diffY, dragStart, isShiftKeyDown, firstDrag, diagram.getRelations());
		}
		// if a single element is selected, drag it (and pass the dragStart, because it's important for Relations)
		else if (selector.getSelectedElements().size() == 1) {
			draggedGridElement.drag(Collections.<Direction> emptySet(), diffX, diffY, dragStart, isShiftKeyDown, firstDrag, diagram.getRelations());
		} else { // if != 1 elements are selected, move them
			moveSelectedElements(diffX, diffY, firstDrag);
		}
		redraw(false);
	}

	public void onMouseMove(Point absolute) {
		GridElement geOnPosition = getGridElementOnPosition(absolute);
		if (geOnPosition != null) { // exactly one gridelement selected which is at the mouseposition
			resizeDirection = geOnPosition.getResizeArea(absolute.getX() - geOnPosition.getRectangle().getX(), absolute.getY() - geOnPosition.getRectangle().getY());
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
		} else {
			resizeDirection.clear();
			Utils.showCursor(Style.Cursor.MOVE);
		}
	}

	public void onShowMenu(Point point) {
		Point relativePoint = new Point(point.x - getAbsoluteLeft(), point.y - getAbsoluteTop());
		if (getGridElementOnPosition(relativePoint) == null) { // gridelement check must be made with relative coordinates
			diagramContextMenu.show(point);
		} else {
			elementContextMenu.show(point);
		}
	}

	public void handleKeyDown(KeyDownEvent event) {
		//		boolean isZoomKey = Shortcut.ZOOM_IN.matches(event) || Shortcut.ZOOM_OUT.matches(event) ||Shortcut.ZOOM_RESET.matches(event);
		//		if (!isZoomKey && !Shortcut.FULLSCREEN.matches(event)) {
		//			event.preventDefault(); // avoid most browser key handlings
		//		}

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
		else if (Shortcut.SAVE.matches(event)) {
			mainView.getSaveCommand().execute();
		}
		else if (Shortcut.MOVE_UP.matches(event)) {
			moveSelectedElements(0, -SharedConstants.DEFAULT_GRID_SIZE, true);
			redraw();
		}
		else if (Shortcut.MOVE_DOWN.matches(event)) {
			moveSelectedElements(0, SharedConstants.DEFAULT_GRID_SIZE, true);
			redraw();
		}
		else if (Shortcut.MOVE_LEFT.matches(event)) {
			moveSelectedElements(-SharedConstants.DEFAULT_GRID_SIZE, 0, true);
			redraw();
		}
		else if (Shortcut.MOVE_RIGHT.matches(event)) {
			moveSelectedElements(SharedConstants.DEFAULT_GRID_SIZE, 0, true);
			redraw();
		} else {
			avoidBrowserDefault = false;
		}

		// avoid browser default key handling for all overwritten keys, but not for others (like F5 for refresh or the zoom controls)
		if (avoidBrowserDefault) {
			event.preventDefault();
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
}
