package com.baselet.gwt.client.element;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.GridElement;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.umlet.element.experimental.ComponentInterface;

public class GwtCanvasElementImpl implements ComponentInterface {

	boolean redrawNecessary = true;
	
	private Canvas canvas = Canvas.createIfSupported();
	private DrawHandlerGWT drawer = new DrawHandlerGWT(canvas);

	private Canvas metaCanvas = Canvas.createIfSupported();
	private DrawHandlerGWT metadrawer = new DrawHandlerGWT(metaCanvas);

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
		redrawNecessary = true;
	}

	@Override
	public BaseDrawHandler getDrawHandler() {
		return drawer;
	}

	@Override
	public BaseDrawHandler getMetaDrawHandler() {
		return metadrawer;
	}

	public void drawOn(Context2d context) {
		if (redrawNecessary) {
			redrawNecessary = false;
			drawer.clearCanvas();
			drawer.drawAll(element.isSelected());
			metadrawer.clearCanvas();
			metadrawer.drawAll();
		}
		context.drawImage(canvas.getCanvasElement(), element.getRectangle().getX(), element.getRectangle().getY());
		context.drawImage(metaCanvas.getCanvasElement(), element.getRectangle().getX(), element.getRectangle().getY());
	}

}
