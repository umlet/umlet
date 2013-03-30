package com.baselet.gwt.client.newclasses;

import com.baselet.control.NewGridElementConstants;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.GridElement;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.umlet.element.experimental.ComponentInterface;

public class GwtCanvasElementImpl implements ComponentInterface {

	boolean redrawNecessary = true;
	
	private Canvas canvas = Canvas.createIfSupported();

	private DrawHandlerGWT drawer = new DrawHandlerGWT(canvas);

	private GridElement element;
	
	private Rectangle rect;

	public GwtCanvasElementImpl(GridElement element) {
		drawer.setBackgroundColor(ColorOwn.BLUE);
		this.element = element;
	}

	@Override
	public void setBoundsRect(Rectangle rect) {
		this.rect = rect;
		}

	@Override
	public Rectangle getBoundsRect() {
		return rect;
		}

	@Override
	public void repaintComponent() {
	}

	@Override
	public BaseDrawHandler getDrawHandler() {
		return drawer;
	}

	@Override
	public BaseDrawHandler getMetaDrawHandler() {
		return drawer;
	}

	public void drawOn(Context2d context) {
		if (redrawNecessary) {
			redrawNecessary = false;
			drawer.clearCanvas();
			drawer.drawAll();
		}
		context.drawImage(canvas.getCanvasElement(), element.getRectangle().getX(), element.getRectangle().getY());
	}

	/**
	 * every model update triggers redrawing the canvas the next time it is used
	 */
	@Override
	public void afterModelUpdate() {
		redrawNecessary = true;
	}

}
