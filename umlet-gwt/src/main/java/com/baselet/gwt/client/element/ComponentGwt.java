package com.baselet.gwt.client.element;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.interfaces.Component;
import com.baselet.element.interfaces.GridElement;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;

public class ComponentGwt implements Component {

	boolean redrawNecessary = true;

	private final Canvas canvas = Canvas.createIfSupported();
	private final DrawHandlerGwt drawer;
	private final DrawHandlerGwt metadrawer;

	private final GridElement element;

	private Rectangle rect;
	private double scaling;

	public ComponentGwt(GridElement element) {
		this(element, 1d);
	}

	public ComponentGwt(GridElement element, double scaling) {
		this.element = element;
		drawer = new DrawHandlerGwt(canvas, scaling);
		metadrawer = new DrawHandlerGwt(canvas, scaling);
		this.scaling = scaling;
	}

	@Override
	public void setBoundsRect(Rectangle rect) {
		this.rect = rect;
	}

	@Override
	public Rectangle getBoundsRect() {
		return rect.copy();
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
	public DrawHandler getDrawHandler() {
		return drawer;
	}

	@Override
	public DrawHandler getMetaDrawHandler() {
		return metadrawer;
	}

	@Override
	public void translateForExport() {
		// currently no export translation necessary
	}

	private boolean lastSelected = false;

	public void drawOn(Context2d context, boolean isSelected) {
		drawOn( context,  isSelected,  1d);
	}

	public void drawOn(Context2d context, boolean isSelected, double scaling) {
		drawer.setNewScaling(scaling);
		metadrawer.setNewScaling(scaling);
		if (redrawNecessary || lastSelected != isSelected) {
			redrawNecessary = false;
			CanvasElement el = canvas.getCanvasElement();
			canvas.getContext2d().clearRect(0, 0, el.getWidth()*scaling, el.getHeight()*scaling);
			canvas.getCanvasElement().setWidth(((int)((double)rect.getWidth()*scaling)) + (int)Math.ceil(1d*scaling)); // canvas size is +1px to make sure a rectangle with width pixels is still visible (in Swing the bound-checking happens in BaseDrawHandlerSwing because you cannot extend the clipping area)
			canvas.getCanvasElement().setHeight(((int)((double)rect.getHeight()*scaling)) + (int)Math.ceil(1d*scaling));
			drawer.drawAll(isSelected);
			if (isSelected) {
				metadrawer.drawAll();
			}
		}
		lastSelected = isSelected;
		context.drawImage(canvas.getCanvasElement(), element.getRectangle().getX()*scaling, element.getRectangle().getY()*scaling);
	}

}
