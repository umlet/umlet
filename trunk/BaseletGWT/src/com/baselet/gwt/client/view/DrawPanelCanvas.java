package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.List;

import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.baselet.gwt.client.EventBus;
import com.baselet.gwt.client.EventBus.PropertiesTextChanged;
import com.baselet.gwt.client.EventBus.PropertiesTextChanged.PropertiesTextChangedEventHandler;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.element.GwtCanvasElementImpl;
import com.baselet.gwt.client.view.MouseDragUtils.MouseDragHandler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class DrawPanelCanvas {

	public final static int GRID_SIZE = 10;

	public static final CssColor GRAY = CssColor.make("rgba(" + 100 + ", " + 100 + "," + 100 + ", " + 0.2 + ")");
	public static final CssColor WHITE = CssColor.make(255, 255, 255);

	private List<GridElement> gridElements = new ArrayList<GridElement>();

	private Canvas elementCanvas;

	private Canvas backgroundCanvas;
	
	private EventBus eventBus = EventBus.getInstance();
	
	private SelectorNew selector = new SelectorNew();
	
	public DrawPanelCanvas() {
		elementCanvas = Canvas.createIfSupported();
		backgroundCanvas = Canvas.createIfSupported();
		
		eventBus.addHandler(PropertiesTextChanged.TYPE, new PropertiesTextChangedEventHandler() {
			@Override
			public void onPropertiesTextChange(PropertiesTextChanged event) {
				for (GridElement ge : gridElements) {
					ge.setPanelAttributes(event.getPropertiesText());
				}
			}
		});

		MouseDragUtils.addMouseDragHandler(this, new MouseDragHandler() {
			@Override
			public void onMouseDrag(int diffX, int diffY, GridElement gridElement) {
				if (gridElement == null) { // nothing selected -> move whole diagram
					for (GridElement ge : gridElements) {
						ge.setLocationDifference(diffX, diffY);
					}
				} else {
					gridElement.setLocationDifference(diffX, diffY);
				}
				draw();
			}
		});

		setCanvasSize(backgroundCanvas, 5000, 5000);
		drawBackgroundGrid();
		draw();
	}

	private void drawBackgroundGrid() {
		int width = backgroundCanvas.getCoordinateSpaceWidth();
		int height = backgroundCanvas.getCoordinateSpaceHeight();
		Context2d backgroundContext = backgroundCanvas.getContext2d();
		backgroundContext.setStrokeStyle(GRAY);
		for (int i = 0; i < width; i += GRID_SIZE) {
			drawLine(backgroundContext, i, 0, i, height);
		}
		for (int i = 0; i < height; i += GRID_SIZE) {
			drawLine(backgroundContext, 0, i, width, i);
		}
	}
	
	private static void drawLine(Context2d context, int x, int y, int x2, int y2) {
		context.beginPath();
		context.moveTo(x + 0.5, y + 0.5); // +0.5 because a line of thickness 1.0 spans 50% left and 50% right (therefore it would not be on the 1 pixel - see https://developer.mozilla.org/en-US/docs/HTML/Canvas/Tutorial/Applying_styles_and_colors)
		context.lineTo(x2 + 0.5, y2 + 0.5);
		context.stroke();
	}

	private void draw() {
		recalculateCanvasSize();
		Context2d context = elementCanvas.getContext2d();
		context.setFillStyle(WHITE);
		context.fillRect(-1000000, -1000000, 2000000, 2000000);
		for (GridElement ge : gridElements) {
			((GwtCanvasElementImpl) ge.getComponent()).drawOn(context);
		}
		context.drawImage(backgroundCanvas.getCanvasElement(), 0, 0);

	}

	Canvas getCanvas() {
		return elementCanvas;
	}

	public GridElement getGridElementOnPosition(int x, int y) {
		for (GridElement ge : gridElements) {
			if (ge.getRectangle().contains(new Point(x, y))) return ge;
		}
		return null;
	}

	public void setGridElements(List<GridElement> gridElements) {
		this.gridElements = gridElements;
		draw();
	}

	private int minWidth, minHeight;
	
	public void setMinSize(int minWidth, int minHeight) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		draw();
	}
	
	private void recalculateCanvasSize() {
		int width = minWidth;
		int height = minHeight;
		for (GridElement ge : gridElements) {
			width = Math.max(ge.getRectangle().getX2(), width);
			height = Math.max(ge.getRectangle().getY2(), height);
		}
		setCanvasSize(elementCanvas, width, height);
	}
	
	private void setCanvasSize(Canvas canvas, int width, int height) {
		canvas.setCoordinateSpaceWidth(width);
		canvas.setWidth(width + "px");
		canvas.setCoordinateSpaceHeight(height);
		canvas.setHeight(height + "px");
	}

	public String toXml() {
		return OwnXMLParser.createXml(this);
	}

	public List<GridElement> getGridElements() {
		return gridElements;
	}
	
	public SelectorNew getSelector() {
		return selector;
	}

}
