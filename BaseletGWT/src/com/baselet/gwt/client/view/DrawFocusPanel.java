package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baselet.control.NewGridElementConstants;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.commandnew.CanAddAndRemoveGridElement;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.baselet.gwt.client.KeyCodesExt;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.Utils;
import com.baselet.gwt.client.element.GwtComponent;
import com.baselet.gwt.client.view.MouseDragUtils.MouseDragHandler;
import com.baselet.gwt.client.view.widgets.OwnTextArea;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.FocusPanel;

public class DrawFocusPanel extends FocusPanel implements CanAddAndRemoveGridElement {

	public static final CssColor GRAY = CssColor.make("rgba(" + 100 + ", " + 100 + "," + 100 + ", " + 0.2 + ")");
	public static final CssColor WHITE = CssColor.make(255, 255, 255);

	private List<GridElement> gridElements = new ArrayList<GridElement>();

	private Canvas elementCanvas;

	private Canvas backgroundCanvas;

	private SelectorNew selector = new SelectorNew();

	private CommandInvoker commandInvoker = new CommandInvoker(this);

	public DrawFocusPanel(final MainView mainView, final PropertiesTextArea propertiesPanel) {
		elementCanvas = Canvas.createIfSupported();
		backgroundCanvas = Canvas.createIfSupported();

		this.add(elementCanvas);

		propertiesPanel.addInstantValueChangeHandler(new InstantValueChangeHandler() {
			@Override
			public void onValueChange(String value) {
				GridElement gridElement = propertiesPanel.getGridElement();
				if (gridElement != null) {
					gridElement.setPanelAttributes(value);
					gridElement.repaint();
				}
				redraw();
			}
		});

		MouseDragUtils.addMouseDragHandler(this, new MouseDragHandler() {
			@Override
			public void onMouseDown(GridElement element, boolean isControlKeyDown) {
				// Set Focus (to make key-shortcuts work)
				DrawFocusPanel.this.setFocus(true);

				if (isControlKeyDown) {
					if (element != null) {
						if (element.isSelected()) {
							selector.deselect(element);
						} else {
							selector.select(element);
							propertiesPanel.setGridElement(element);
						}
					}
				} else {
					if (element != null) {
						if (!selector.isSelected(element)) {
							selector.selectOnly(element);
						}
						propertiesPanel.setGridElement(element);
					} else {
						selector.deselectAll();
					}
				}

				redraw();
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
					for (GridElement ge : gridElements) {
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
				if (geOnPosition != null && selector.isSelectedOnly(geOnPosition)) {
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
					Utils.showCursor(Style.Cursor.DEFAULT);
				}
			}
		});

		getCanvas().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				GridElement ge = getGridElementOnPosition(new Point(event.getX(), event.getY()));
				if (ge != null) {
					GridElement e = ge.CloneFromMe();
					e.setLocationDifference(NewGridElementConstants.DEFAULT_GRID_SIZE, NewGridElementConstants.DEFAULT_GRID_SIZE);
					commandInvoker.addElements(e);
				}
			}
		});
		this.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				int code = event.getNativeKeyCode();

				boolean zoomControls = event.isControlKeyDown() && KeyCodesExt.isZoomKey(code);
				if (!zoomControls && !KeyCodesExt.isSwitchToFullscreen(code)) {
					event.preventDefault(); // avoid most browser key handlings
				}

				if (code == KeyCodes.KEY_DELETE) {
					commandInvoker.removeSelectedElements();
				}
				else if (event.isControlKeyDown() && code == 'C') {
					commandInvoker.copySelectedElements();
				}
				else if (event.isControlKeyDown() && code == 'X') {
					commandInvoker.cutSelectedElements();
				}
				else if (event.isControlKeyDown() && code == 'V') {
					commandInvoker.pasteElements();
				}
				else if (event.isControlKeyDown() && code == 'S') {
					mainView.getSaveCommand().execute();
				}

			}
		});

		clearAndSetCanvasSize(backgroundCanvas, 5000, 5000);
		if (Location.getParameter("grid") != null) {
			drawBackgroundGrid();
		}
		redraw();
	}

	private void drawBackgroundGrid() {
		int width = backgroundCanvas.getCoordinateSpaceWidth();
		int height = backgroundCanvas.getCoordinateSpaceHeight();
		Context2d backgroundContext = backgroundCanvas.getContext2d();
		backgroundContext.setStrokeStyle(GRAY);
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

	private void redraw() {
		clearAndRecalculateCanvasSize();
		Context2d context = elementCanvas.getContext2d();
		for (GridElement ge : gridElements) {
			((GwtComponent) ge.getComponent()).drawOn(context);
		}
		context.drawImage(backgroundCanvas.getCanvasElement(), 0, 0);

	}

	Canvas getCanvas() {
		return elementCanvas;
	}

	public GridElement getGridElementOnPosition(Point point) {
		List<GridElement> elementsOnPosition = new ArrayList<GridElement>();
		for (GridElement ge : gridElements) {
			if (ge.isSelectableOn(point)) {
				elementsOnPosition.add(ge);
			}
		}
		for (GridElement ge : elementsOnPosition) { // selected elements have priority
			if (selector.isSelected(ge)) {
				return ge;
			}
		}
		if (!elementsOnPosition.isEmpty()) { // TODO implement algorithm that smallest element will be returned
			return elementsOnPosition.get(0);
		}
		return null;
	}

	public void setGridElements(List<GridElement> gridElements) {
		this.gridElements = gridElements;
		redraw();
	}

	@Override
	public void addGridElements(GridElement ... elements) {
		this.gridElements.addAll(Arrays.asList(elements));
		selector.selectOnly(elements);
		redraw();
	}

	@Override
	public void removeGridElements(GridElement ... elements) {
		this.gridElements.removeAll(Arrays.asList(elements));
		selector.deselect(elements);
		redraw();
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
		for (GridElement ge : gridElements) {
			width = Math.max(ge.getRectangle().getX2(), width);
			height = Math.max(ge.getRectangle().getY2(), height);
		}
		clearAndSetCanvasSize(elementCanvas, width, height);
	}

	private void clearAndSetCanvasSize(Canvas canvas, int width, int height) {
		// setCoordinateSpace always clears the canvas. To avoid that see https://groups.google.com/d/msg/google-web-toolkit/dpc84mHeKkA/3EKxrlyFCEAJ
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
	}

	public String toXml() {
		return OwnXMLParser.gridElementsToXml(getGridElements());
	}

	public List<GridElement> getGridElements() {
		return gridElements;
	}

	public SelectorNew getSelector() {
		return selector;
	}

	public CommandInvoker getCommandInvoker() {
		return commandInvoker;
	}
}
