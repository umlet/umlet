package com.baselet.gwt.client.view;

import java.util.List;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.enums.ElementId;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.ComponentGwt;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.jsinterop.Context2dGwtWrapper;
import com.baselet.gwt.client.jsinterop.Context2dWrapper;
import com.baselet.gwt.client.resources.HelptextFactory;
import com.baselet.gwt.client.resources.HelptextResources;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.FocusWidget;

public class DrawCanvas extends CanvasWrapper {

	private final Canvas canvas = Canvas.createIfSupported();

	public FocusWidget getWidget() {
		return canvas;
	}

	@Override
	public Context2dWrapper getContext2d() {
		return new Context2dGwtWrapper(canvas.getContext2d());
	}

	@Override
	public void clearAndSetSize(int width, int height) {
		// setCoordinateSpace always clears the canvas. To avoid that see https://groups.google.com/d/msg/google-web-toolkit/dpc84mHeKkA/3EKxrlyFCEAJ
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
	}

	@Override
	public int getWidth() {
		return canvas.getCoordinateSpaceWidth();
	}

	@Override
	public int getHeight() {
		return canvas.getCoordinateSpaceHeight();
	}

	public CanvasElement getCanvasElement() {
		return canvas.getCanvasElement();
	}

	@Override
	public String toDataUrl(String type) {
		return canvas.toDataUrl(type);
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

	/* used to diaply temporal invalid if vs code passes a wrong uxf */
	void drawInvalidDiagramInfo() {
		double elWidth = 440;
		double elHeight = 246;
		double elXPos = getWidth() / 2.0 - elWidth / 2;
		double elYPos = getHeight() / 2.0 - elHeight / 2;
		String invalidDiagramText = "valign=center\n" +
									"halign=center\n" +
									".uxf file is currently invalid and can't be displayed.\n" +
									"Please revert changes or load a valid file";
		GridElement emptyElement = ElementFactoryGwt.create(ElementId.Text, new Rectangle(elXPos, elYPos, elWidth, elHeight), invalidDiagramText, "", null);
		((ComponentGwt) emptyElement.getComponent()).drawOn(getContext2d(), false, getScaling());
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
