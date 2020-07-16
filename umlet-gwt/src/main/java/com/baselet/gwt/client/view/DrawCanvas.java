package com.baselet.gwt.client.view;

import java.util.List;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.enums.ElementId;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.ComponentGwt;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.FocusWidget;

public class DrawCanvas {
	public interface HelptextResources extends ClientBundle {
		HelptextResources INSTANCE = GWT.create(HelptextResources.class);

		@Source("Helptext.txt")
		TextResource helpText();

		@Source("Helptext_vscode.txt")
		TextResource helpTextVsCode();
	}

	private final Canvas canvas = Canvas.createIfSupported();

	public FocusWidget getWidget() {
		return canvas;
	}

	public Context2d getContext2d() {
		return canvas.getContext2d();
	}

	public void clearAndSetSize(int width, int height) {
		// setCoordinateSpace always clears the canvas. To avoid that see https://groups.google.com/d/msg/google-web-toolkit/dpc84mHeKkA/3EKxrlyFCEAJ
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
	}

	public int getWidth() {
		return canvas.getCoordinateSpaceWidth();
	}

	public int getHeight() {
		return canvas.getCoordinateSpaceHeight();
	}

	public CanvasElement getCanvasElement() {
		return canvas.getCanvasElement();
	}

	public String toDataUrl(String type) {
		return canvas.toDataUrl(type);
	}

	/*
		setScaling can be used to set the size the canvas for the next time draw() is used

		DISCLAIMER: if scaling is set to anything other than 1, this WILL break the way selected elements are viewed,
		furthermore the dragging will still remain the same and will not update to the new scale.
		This function is solely meant to be used for high-res exporting of the diagram
	 */
	private double scaling = 1.0d;
	private boolean scaleHasChangedSinceLastDraw = false;
	public void setScaling(double scaling)
	{
	this.scaling = scaling;
		scaleHasChangedSinceLastDraw = true;
	}

	void draw(boolean drawEmptyInfo, List<GridElement> gridElements, Selector selector, boolean forceRedraw) {

		if (SharedConfig.getInstance().isDev_mode()) {
			CanvasUtils.drawGridOn(getContext2d());
		}

		if (drawEmptyInfo && gridElements.isEmpty()) {
			drawEmptyInfoText(scaling);
		}
		else {
			// if (tryOptimizedDrawing()) return;
			for (GridElement ge : gridElements) {
				if (forceRedraw)
					((ComponentGwt) ge.getComponent()).afterModelUpdate();
				((ComponentGwt) ge.getComponent()).drawOn(canvas.getContext2d(), selector.isSelected(ge), scaling);
			}
		}
	}

	void draw(boolean drawEmptyInfo, List<GridElement> gridElements, Selector selector) {
		if (scaleHasChangedSinceLastDraw)
		{
			draw(drawEmptyInfo, gridElements,  selector, true);
			scaleHasChangedSinceLastDraw = false;
		} else {
			draw(drawEmptyInfo, gridElements,  selector, false);
		}
	}

	private void drawEmptyInfoText(double scaling) {
		double elWidth = 440;
		double elHeight = 246; //web version 150
		double elXPos = getWidth() / 2.0 - elWidth / 2;
		double elYPos = getHeight() / 2.0 - elHeight / 2;
		String helptext = HelptextResources.INSTANCE.helpText().getText();
		GridElement emptyElement = ElementFactoryGwt.create(ElementId.Text, new Rectangle(elXPos, elYPos, elWidth, elHeight), helptext, "", null);
		((ComponentGwt) emptyElement.getComponent()).drawOn(canvas.getContext2d(), false, scaling);
	}

	// TODO would not work because canvas gets always resized and therefore cleaned -> so everything must be redrawn
	// private boolean tryOptimizedDrawing() {
	// List<GridElement> geToRedraw = new ArrayList<GridElement>();
	// for (GridElement ge : gridElements) {
	// if(((GwtComponent) ge.getComponent()).isRedrawNecessary()) {
	// for (GridElement geRedraw : geToRedraw) {
	// if (geRedraw.getRectangle().intersects(ge.getRectangle())) {
	// return false;
	// }
	// }
	// geToRedraw.add(ge);
	// }
	// }
	//
	// for (GridElement ge : gridElements) {
	// elementCanvas.getContext2d().clearRect(0, 0, ge.getRectangle().getWidth(), ge.getRectangle().getHeight());
	// ((GwtComponent) ge.getComponent()).drawOn(elementCanvas.getContext2d());
	// }
	// return true;
	// }
}
