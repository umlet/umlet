package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.baselet.control.NewGridElementConstants;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.commandnew.CanAddAndRemoveGridElement;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.gwt.client.Converter;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.Utils;
import com.baselet.gwt.client.element.Diagram;
import com.baselet.gwt.client.element.GwtComponent;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.view.MouseDoubleClickUtils.Handler;
import com.baselet.gwt.client.view.MouseUtils.MouseDragHandler;
import com.baselet.gwt.client.view.widgets.MenuPopup;
import com.baselet.gwt.client.view.widgets.MenuPopup.MenuPopupItem;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.umlet.element.experimental.element.uml.relation.Relation;

public abstract class DrawFocusPanel extends FocusPanel implements CanAddAndRemoveGridElement {

	private Diagram diagram = new Diagram(new ArrayList<GridElement>());

	private Canvas elementCanvas;

	private Canvas backgroundCanvas;

	Selector selector;

	CommandInvoker commandInvoker = CommandInvoker.getInstance();

	DrawFocusPanel otherDrawFocusPanel;
	
	private boolean hasFocus = false;

	public void setOtherDrawFocusPanel(DrawFocusPanel otherDrawFocusPanel) {
		this.otherDrawFocusPanel = otherDrawFocusPanel;
	}
	
	public void setHasFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}

	public DrawFocusPanel(final MainView mainView, final PropertiesTextArea propertiesPanel) {
		selector = new SelectorNew() {
			public void doAfterSelectionChanged() {
				if (getSelectedElements().size() == 1) {
					propertiesPanel.setGridElement(getSelectedElements().get(0));
				} else {
					propertiesPanel.setGridElement(diagram);
				}
				redraw();
			}
		};
		elementCanvas = Canvas.createIfSupported();
		backgroundCanvas = Canvas.createIfSupported();

		MenuPopup.appendTo(elementCanvas, new MenuPopupItem() {
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


		this.add(elementCanvas);

		propertiesPanel.addInstantValueChangeHandler(new InstantValueChangeHandler() {
			@Override
			public void onValueChange(String value) {
				propertiesPanel.updateElementOrHelptext();
				redraw();
			}
		});

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

		MouseUtils.addMouseHandler(this, new MouseDragHandler() {
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
						if (!selector.isSelected(element)) {
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
						ge.drag(Collections.<Direction> emptySet(), diffX, diffY, dragStart, isShiftKeyDown, firstDrag);
					}
				} else {
					draggedGridElement.drag(resizeDirection, diffX, diffY, dragStart, isShiftKeyDown, firstDrag);
				}
				redraw();
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
		});

		MouseDoubleClickUtils.addMouseDragHandler(this, new Handler() {
			@Override
			public void onDoubleClick(GridElement ge) {
				if (ge != null) {
					doDoubleClickAction(ge);
				}
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

		if (NewGridElementConstants.isDevMode) {
			clearAndSetCanvasSize(backgroundCanvas, 5000, 5000);
			drawBackgroundGrid();
		}
	}

	private void drawBackgroundGrid() {
		int width = backgroundCanvas.getCoordinateSpaceWidth();
		int height = backgroundCanvas.getCoordinateSpaceHeight();
		Context2d backgroundContext = backgroundCanvas.getContext2d();
		backgroundContext.setStrokeStyle(Converter.convert(ColorOwn.BLACK.transparency(Transparency.SELECTION_BACKGROUND)));
		for (int i = 0; i < width; i += NewGridElementConstants.DEFAULT_GRID_SIZE) {
			drawLine(backgroundContext, i, 0, i, height);
		}
		for (int i = 0; i < height; i += NewGridElementConstants.DEFAULT_GRID_SIZE) {
			drawLine(backgroundContext, 0, i, width, i);
		}
	}

	private static void drawLine(Context2d context, int x, int y, int x2, int y2) {
		context.beginPath();
		context.moveTo(x + 0.5, y + 0.5); // +0.5 because a line of thickness 1.0 spans 50% left and 50% right (therefore it would not be on the 1 pixel - see https://developer.mozilla.org/en-US/docs/HTML/Canvas/Tutorial/Applying_styles_and_colors)
		context.lineTo(x2 + 0.5, y2 + 0.5);
		context.stroke();
	}

	void redraw() {
		clearAndRecalculateCanvasSize();
		Context2d context = elementCanvas.getContext2d();

		if (NewGridElementConstants.isDevMode) {
			context.drawImage(backgroundCanvas.getCanvasElement(), 0, 0);
		}

		if (diagram.getGridElements().isEmpty()) {
			diagram.drawEmptyInfoText(elementCanvas);
		} else {
			//		if (tryOptimizedDrawing()) return;
			for (GridElement ge : diagram.getGridElementsSorted()) {
				((GwtComponent) ge.getComponent()).drawOn(context, selector.isSelected(ge));
			}
		}

	}

	//TODO would not work because canvas gets always resized and therefore cleaned -> so everything must be redrawn
	//	private boolean tryOptimizedDrawing() {
	//		List<GridElement> geToRedraw = new ArrayList<GridElement>();
	//		for (GridElement ge : gridElements) {
	//			if(((GwtComponent) ge.getComponent()).isRedrawNecessary()) {
	//				for (GridElement geRedraw : geToRedraw) {
	//					if (geRedraw.getRectangle().intersects(ge.getRectangle())) {
	//						return false;
	//					}
	//				}
	//				geToRedraw.add(ge);
	//			}
	//		}
	//
	//		for (GridElement ge : gridElements) {
	//			elementCanvas.getContext2d().clearRect(0, 0, ge.getRectangle().getWidth(), ge.getRectangle().getHeight());
	//			((GwtComponent) ge.getComponent()).drawOn(elementCanvas.getContext2d());
	//		}
	//		return true;
	//	}

	Canvas getCanvas() {
		return elementCanvas;
	}

	public GridElement getGridElementOnPosition(Point point) {
		GridElement returnGe = null;
		returnGe = getGridElementOnPositionHelper(point, selector.getSelectedElements());
		if (returnGe == null) { // if no selected element is found, search all elements
			returnGe = getGridElementOnPositionHelper(point, diagram.getGridElements());
		}
		return returnGe;
	}

	private GridElement getGridElementOnPositionHelper(Point point, Collection<GridElement> elements) {
		GridElement returnGe = null;
		for (GridElement ge : elements) {
			if (ge.isSelectableOn(point)) {
				if (returnGe == null) {
					returnGe = ge;
				} else {
					boolean newIsRelationOldNot = ((ge instanceof Relation) && (!(returnGe instanceof Relation)));
					boolean oldContainsNew = returnGe.getRectangle().contains(ge.getRectangle());
					if (newIsRelationOldNot || oldContainsNew) {
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

	private int minWidth, minHeight;

	public void setMinSize(int minWidth, int minHeight) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		redraw();
	}

	private void clearAndRecalculateCanvasSize() {
		int width = minWidth;
		int height = minHeight;
		for (GridElement ge : diagram.getGridElements()) {
			width = Math.max(ge.getRectangle().getX2(), width);
			height = Math.max(ge.getRectangle().getY2(), height);
		}
		clearAndSetCanvasSize(elementCanvas, width, height);
	}

	abstract void doDoubleClickAction(GridElement ge);

	private void clearAndSetCanvasSize(Canvas canvas, int width, int height) {
		// setCoordinateSpace always clears the canvas. To avoid that see https://groups.google.com/d/msg/google-web-toolkit/dpc84mHeKkA/3EKxrlyFCEAJ
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
	}

	public String toXml() {
		return OwnXMLParser.diagramToXml(diagram);
	}

	public Diagram getDiagram() {
		return diagram;
	}

	public Selector getSelector() {
		return selector;
	}
}
