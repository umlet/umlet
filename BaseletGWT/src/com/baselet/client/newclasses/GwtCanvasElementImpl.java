package com.baselet.client.newclasses;

import com.baselet.client.copy.control.NewGridElementConstants;
import com.baselet.client.copy.diagram.draw.BaseDrawHandler;
import com.baselet.client.copy.diagram.draw.geom.Rectangle;
import com.baselet.client.copy.diagram.draw.helper.ColorOwn;
import com.baselet.client.copy.element.GridElement;
import com.baselet.client.copy.umlet.element.experimental.ComponentInterface;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

public class GwtCanvasElementImpl implements ComponentInterface {

	boolean redrawNecessary = true;
	
	private Canvas canvas = Canvas.createIfSupported();

	private DrawHandlerGWT drawer = new DrawHandlerGWT(canvas);

	private GridElement element;
	
	private Rectangle rect;

	public GwtCanvasElementImpl(GridElement element) {
		drawer.setBackground(ColorOwn.BLUE, NewGridElementConstants.ALPHA_NEARLY_FULL_TRANSPARENCY);
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
