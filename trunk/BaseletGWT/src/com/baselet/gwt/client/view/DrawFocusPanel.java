package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.baselet.control.SharedUtils;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.commandnew.CanAddAndRemoveGridElement;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.gwt.client.Browser;
import com.baselet.gwt.client.Utils;
import com.baselet.gwt.client.element.Diagram;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.view.MouseUtils.MouseDragHandler;
import com.baselet.gwt.client.view.widgets.MenuPopup;
import com.baselet.gwt.client.view.widgets.MenuPopup.MenuPopupItem;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.FocusPanel;

public abstract class DrawFocusPanel extends FocusPanel implements CanAddAndRemoveGridElement {

	private static final Logger log = Logger.getLogger(DrawFocusPanel.class);

	private Diagram diagram = new Diagram(new ArrayList<GridElement>());

	private DrawCanvas canvas = new DrawCanvas();

	Selector selector;

	CommandInvoker commandInvoker = CommandInvoker.getInstance();

	DrawFocusPanel otherDrawFocusPanel;

	private boolean hasFocus = false;

	private AutoResizeScrollDropPanel scrollPanel;

	public void setOtherDrawFocusPanel(DrawFocusPanel otherDrawFocusPanel) {
		this.otherDrawFocusPanel = otherDrawFocusPanel;
	}

	public void setHasFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}

	public DrawFocusPanel(final MainView mainView, final PropertiesTextArea propertiesPanel) {
		this.setStylePrimaryName("canvasFocusPanel");

		selector = new SelectorNew() {
			public void doAfterSelectionChanged() {
				if (!getSelectedElements().isEmpty()) { // always set properties text of latest selected element (so you also have an element in the prop panel even if you have an active multiselect)
					propertiesPanel.setGridElement(getSelectedElements().get(getSelectedElements().size()-1), DrawFocusPanel.this);
				} else {
					propertiesPanel.setGridElement(diagram, DrawFocusPanel.this);
				}
				redraw();
			}
		};

		final MenuPopup popup = new MenuPopup (
				new MenuPopupItem() {
					@Override
					public String getText() {
						return "Delete";
					}
					@Override
					public void execute() {
						commandInvoker.removeSelectedElements(DrawFocusPanel.this);
					}
				}, new MenuPopupItem() {
					@Override
					public String getText() {
						return "Copy";
					}
					@Override
					public void execute() {
						commandInvoker.copySelectedElements(DrawFocusPanel.this);
					}
				}, new MenuPopupItem() {
					@Override
					public String getText() {
						return "Cut";
					}
					@Override
					public void execute() {
						commandInvoker.cutSelectedElements(DrawFocusPanel.this);
					}
				}, new MenuPopupItem() {
					@Override
					public String getText() {
						return "Paste";
					}
					@Override
					public void execute() {
						commandInvoker.pasteElements(DrawFocusPanel.this);
					}
				}, new MenuPopupItem() {
					@Override
					public String getText() {
						return "Select All";
					}
					@Override
					public void execute() {
						selector.select(diagram.getGridElements());
					}
				});

		this.add(canvas.getWidget());

		this.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (!hasFocus) { // if focus has switched from diagram <-> palette, reset other selector and redraw
					otherDrawFocusPanel.getSelector().deselectAllWithoutAfterAction();
					otherDrawFocusPanel.redraw(); // redraw is necessary even if other afteractions (properties panel update) are not
					otherDrawFocusPanel.setHasFocus(false);
					setHasFocus(true);
				}
			}
		});

		MouseUtils.addMouseHandler(this, canvas.getWidget(), new MouseDragHandler() {
			@Override
			public void onMouseDown(GridElement element, boolean isControlKeyDown) {
				// Set Focus (to make key-shortcuts work)
				DrawFocusPanel.this.setFocus(true);

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
							propertiesPanel.setGridElement(element, DrawFocusPanel.this);
						} else {
							selector.selectOnly(element);
						}
					} else {
						selector.deselectAll();
					}
				}

			}

			@Override
			public void onMouseDragEnd(GridElement draggedGridElement) {
				if (draggedGridElement != null && selector.isSelectedOnly(draggedGridElement)) {
					draggedGridElement.dragEnd();
				}
				redraw();
			}

			Set<Direction> resizeDirection = new HashSet<Direction>();

			@Override
			public void onMouseMoveDragging(Point dragStart, int diffX, int diffY, GridElement draggedGridElement, boolean isShiftKeyDown, boolean isCtrlKeyDown, boolean firstDrag) {
				if (draggedGridElement == null) { // not dragging a grid element -> move whole diagram
					Utils.showCursor(Style.Cursor.POINTER);
					for (GridElement ge : diagram.getGridElements()) {
						ge.setLocationDifference(diffX, diffY);
					}
				} else if (isCtrlKeyDown) {
					return; // TODO implement Lasso
				} else if (selector.getSelectedElements().size() > 1) {
					for (GridElement ge : selector.getSelectedElements()) {
						ge.drag(Collections.<Direction> emptySet(), diffX, diffY, dragStart, isShiftKeyDown, firstDrag, diagram.getRelations());
					}
				} else {
					draggedGridElement.drag(resizeDirection, diffX, diffY, dragStart, isShiftKeyDown, firstDrag, diagram.getRelations());
				}
				redraw(false);
			}

			@Override
			public void onMouseMove(Point absolute) {
				GridElement geOnPosition = getGridElementOnPosition(absolute);
				if (geOnPosition != null && selector.isSelectedOnly(geOnPosition)) { // exactly one gridelement selected which is at the mouseposition
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
					if (geOnPosition != null) {
						Utils.showCursor(Style.Cursor.POINTER); // HAND Cursor
					} else {
						Utils.showCursor(Style.Cursor.DEFAULT);
					}
				}
			}

			@Override
			public void onDoubleClick(GridElement ge) {
				if (ge != null) {
					doDoubleClickAction(ge);
				}
			}

			@Override
			public void onShowMenu(Point point) {
				popup.show(point);
			}
		});

		this.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				boolean isZoomKey = Shortcut.ZOOM_IN.matches(event) || Shortcut.ZOOM_OUT.matches(event) ||Shortcut.ZOOM_RESET.matches(event);
				if (!isZoomKey && !Shortcut.FULLSCREEN.matches(event)) {
					event.preventDefault(); // avoid most browser key handlings
				}

				if (Shortcut.DELETE_ELEMENT.matches(event)) {
					commandInvoker.removeSelectedElements(DrawFocusPanel.this);
				}
				else if (Shortcut.DESELECT_ALL.matches(event)) {
					selector.deselectAll();
				}
				else if (Shortcut.SELECT_ALL.matches(event)) {
					selector.select(diagram.getGridElements());
				}
				else if (Shortcut.COPY.matches(event)) {
					commandInvoker.copySelectedElements(DrawFocusPanel.this);
				}
				else if (Shortcut.CUT.matches(event)) {
					commandInvoker.cutSelectedElements(DrawFocusPanel.this);
				}
				else if (Shortcut.PASTE.matches(event)) {
					commandInvoker.pasteElements(DrawFocusPanel.this);
				}
				else if (Shortcut.SAVE.matches(event)) {
					mainView.getSaveCommand().execute();
				}

			}
		});
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
		selector.deselectAll(); // necessary to trigger setting helptext to properties
		redraw();
	}

	@Override
	public void addGridElements(GridElement ... elements) {
		diagram.getGridElements().addAll(Arrays.asList(elements));
		selector.selectOnly(elements);
	}

	@Override
	public void removeGridElements(GridElement ... elements) {
		diagram.getGridElements().removeAll(Arrays.asList(elements));
		selector.deselect(elements);
	}

	abstract void doDoubleClickAction(GridElement ge);


	public Diagram getDiagram() {
		return diagram;
	}

	public Selector getSelector() {
		return selector;
	}

	public void setScrollPanel(AutoResizeScrollDropPanel scrollPanel) {
		this.scrollPanel = scrollPanel;
	}

	@Override
	public void setFocus(boolean focused) {
		// Internet explorer scrolls to the top left if canvas gets focus, therefore scroll back afterwards // see http://stackoverflow.com/questions/14979365/table-scroll-bar-jumps-up-when-table-receives-focus-in-ie
		if (Browser.get() == Browser.INTERNET_EXPLORER && focused) {
			int oldH = scrollPanel.getHorizontalScrollPosition();
			int oldV = scrollPanel.getVerticalScrollPosition();
			super.setFocus(focused);
			scrollPanel.setHorizontalScrollPosition(oldH);
			scrollPanel.setVerticalScrollPosition(oldV);
		} else {
			super.setFocus(focused);
		}
	}
}
