package com.baselet.gwt.client.element;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.umlet.element.experimental.Component;

public class ComponentGwt implements Component {

	boolean redrawNecessary = true;
	
	private Canvas canvas = Canvas.createIfSupported();
	private BaseDrawHandlerGWT drawer = new BaseDrawHandlerGWT(canvas);
	private BaseDrawHandlerGWT metadrawer = new BaseDrawHandlerGWT(canvas);

	private GridElement element;
	
	private Rectangle rect;

	public ComponentGwt(GridElement element) {
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

//	public boolean isRedrawNecessary() {
//		return redrawNecessary;
//	}

	private boolean lastSelected = false;
	public void drawOn(Context2d context, boolean isSelected) {
		if (redrawNecessary || lastSelected != isSelected) {
			redrawNecessary = false;
			CanvasElement el = canvas.getCanvasElement();
			canvas.getContext2d().clearRect(0, 0, el.getWidth(), el.getHeight());
			canvas.getCanvasElement().setWidth(rect.getWidth());
			canvas.getCanvasElement().setHeight(rect.getHeight());
			drawer.drawAll(isSelected);
			if (isSelected) {
				metadrawer.drawAll();
			}
		}
		lastSelected = isSelected;
		context.drawImage(canvas.getCanvasElement(), element.getRectangle().getX(), element.getRectangle().getY());
	}

}
