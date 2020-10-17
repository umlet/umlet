package com.baselet.gwt.client.view;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.enums.ElementId;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.ComponentGwt;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.jsinterop.Context2dWrapper;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.resources.HelptextFactory;
import com.baselet.gwt.client.resources.HelptextResources;
import com.google.gwt.core.client.GWT;

import java.util.List;

public abstract class CanvasWrapper {
	/* setScaling can be used to set the size the canvas for the next time draw() is used DISCLAIMER: if scaling is set to anything other than 1, this WILL break the way selected elements are viewed, furthermore the dragging will still remain the same and will not update to the new scale. This function is solely meant to be used for high-res exporting of the diagram */
	private double scaling = 1.0d;
	private boolean scaleHasChangedSinceLastDraw = false;

	private static final CustomLogger logger = CustomLoggerFactory.getLogger(CanvasWrapper.class);

	public abstract Context2dWrapper getContext2d();

	public abstract void clearAndSetSize(int width, int height);

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract String toDataUrl(String type);

	public void setScaling(double scaling) {
		this.scaling = scaling;
		scaleHasChangedSinceLastDraw = true;
	}

	void draw(boolean drawEmptyInfo, List<GridElement> gridElements, Selector selector, boolean forceRedraw) {
		if (SharedConfig.getInstance().isDev_mode()) {
			CanvasUtils.drawGridOn(getContext2d());
		}
		if (drawEmptyInfo && gridElements.isEmpty()) {
			drawEmptyInfoText(getScaling());
		}
		else {
			// if (tryOptimizedDrawing()) return;
			for (GridElement ge : gridElements) {
				if (forceRedraw) {
					((ComponentGwt) ge.getComponent()).afterModelUpdate();
				}
				((ComponentGwt) ge.getComponent()).drawOn(getContext2d(), selector.isSelected(ge), getScaling());
			}
		}
		if (selector instanceof SelectorNew && ((SelectorNew) selector).isLassoActive()) {
			((SelectorNew) selector).drawLasso(getContext2d());
		}
	}

	public void draw(boolean drawEmptyInfo, List<GridElement> gridElements, Selector selector) {
		if (isScaleHasChangedSinceLastDraw()) {
			draw(drawEmptyInfo, gridElements, selector, true);
			setScaleHasChangedSinceLastDraw(false);
		}
		else {
			draw(drawEmptyInfo, gridElements, selector, false);
		}
	}

	public void drawEmptyInfoText(double scaling) {
		double elWidth = 440;
		double elHeight = 246;
		double elXPos = getWidth() / 2.0 - elWidth / 2;
		double elYPos = getHeight() / 2.0 - elHeight / 2;
		HelptextFactory factory = GWT.create(HelptextFactory.class);
		HelptextResources resources = factory.getInstance();
		String helptext = resources.helpText().getText();
		GridElement emptyElement = ElementFactoryGwt.create(ElementId.Text, new Rectangle(elXPos, elYPos, elWidth, elHeight), helptext, "", null);
		((ComponentGwt) emptyElement.getComponent()).drawOn(getContext2d(), false, scaling);
	}

	public double getScaling() {
		return scaling;
	}

	public boolean isScaleHasChangedSinceLastDraw() {
		return scaleHasChangedSinceLastDraw;
	}

	public void setScaleHasChangedSinceLastDraw(boolean scaleHasChangedSinceLastDraw) {
		this.scaleHasChangedSinceLastDraw = scaleHasChangedSinceLastDraw;
	}
}
