package com.baselet.client;

import java.util.ArrayList;
import java.util.List;

import com.baselet.client.MouseDragUtils.MouseDragHandler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class DrawPanelCanvas {

	CssColor red = CssColor.make("rgba(" + 255 + ", " + 0 + "," + 0 + ", " + 1.0 + ")");
	
	List<GridElement> gridElements = new ArrayList<GridElement>();
	
	Canvas canvas;
	
	public DrawPanelCanvas() {
		gridElements.add(new GridElement(new Rectangle(5, 5, 30, 30)));
		gridElements.add(new GridElement(new Rectangle(40, 5, 30, 30)));
		gridElements.add(new GridElement(new Rectangle(40, 40, 30, 30)));
	}
	
	Canvas makeCanvas(int width, int height) {
		canvas = Canvas.createIfSupported();
		canvas.setStyleName("canvas");
		//		canvas.setWidth(width + "px");
		canvas.setCoordinateSpaceWidth(width);
		//		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceHeight(height);

		final Context2d context = canvas.getContext2d();

		MouseDragUtils.addMouseDragHandler(this, new MouseDragHandler() {
			@Override
			public void onMouseDrag(int diffX, int diffY, GridElement gridElement) {
				if (gridElement == null) { // nothing selected -> move whole diagram
					for (GridElement ge : gridElements) {
						ge.getBounds().move(diffX, diffY);
					}
				} else {
					gridElement.getBounds().move(diffX, diffY);
				}
				draw(context);
			}
		});

//		canvas.addMouseWheelHandler(new MouseWheelHandler() {
//			@Override
//			public void onMouseWheel(MouseWheelEvent event) {
//				float zoom;
//				if(event.getDeltaY() < 0) { // scrolling up = negative values, scrolling down = positive values
//					zoom = 1.1f;
//				} else {
//					zoom = 0.9f;
//				}
//				context.scale(zoom,zoom);
//			}
//		});

		draw(context);

		return canvas;
	}

	private void draw(final Context2d context) {
		context.clearRect(-1000000, -1000000, 2000000, 2000000);
		for (GridElement ge : gridElements) {
			context.setFillStyle(red);
			context.fillRect(ge.getBounds().getX(), ge.getBounds().getY(), ge.getBounds().getWidth(), ge.getBounds().getHeight());
		}
		context.fill();
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public GridElement getGridElementOnPosition(int x, int y) {
		for (GridElement ge : gridElements) {
			if (ge.contains(x, y)) return ge;
		}
		return null;
	}
}
