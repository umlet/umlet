package com.baselet.gwt.client.element;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.GridElement;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.umlet.element.experimental.ComponentInterface;

public class GwtComponent implements ComponentInterface {

	boolean redrawNecessary = true;
	
	private Canvas canvas = Canvas.createIfSupported();
	private BaseDrawHandlerGWT drawer = new BaseDrawHandlerGWT(canvas);

	private Canvas metaCanvas = Canvas.createIfSupported();
	private BaseDrawHandlerGWT metadrawer = new BaseDrawHandlerGWT(metaCanvas);

	private GridElement element;
	
	private Rectangle rect;

	public GwtComponent(GridElement element) {
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
		// repainting is currently not controlled by the gridelement itself
	}
	
	@Override
	public void afterModelUpdate() {
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
			canvas.getCanvasElement().setWidth(rect.getWidth());
			canvas.getCanvasElement().setHeight(rect.getHeight());
			drawer.drawAll(element.isSelected());
			
			metadrawer.clearCanvas();
			metaCanvas.getCanvasElement().setWidth(rect.getWidth());
			metaCanvas.getCanvasElement().setHeight(rect.getHeight());
			metadrawer.drawAll();
		}
		context.drawImage(canvas.getCanvasElement(), element.getRectangle().getX(), element.getRectangle().getY());
		context.drawImage(metaCanvas.getCanvasElement(), element.getRectangle().getX(), element.getRectangle().getY());
	}

}
