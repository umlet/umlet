package com.baselet.gwt.client.view;

import com.baselet.control.NewGridElementConstants;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.gwt.client.Converter;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;

public class BackgroundCanvas {
	
	Canvas canvas = Canvas.createIfSupported();

	void drawBackgroundGrid() {
		canvas.setCoordinateSpaceWidth(3000);
		canvas.setCoordinateSpaceHeight(2000);
		int width = canvas.getCoordinateSpaceWidth();
		int height = canvas.getCoordinateSpaceHeight();
		Context2d backgroundContext = canvas.getContext2d();
		backgroundContext.setStrokeStyle(Converter.convert(ColorOwn.BLACK.transparency(Transparency.SELECTION_BACKGROUND)));
		for (int i = 0; i < width; i += NewGridElementConstants.DEFAULT_GRID_SIZE) {
			drawLine(backgroundContext, i, 0, i, height);
		}
		for (int i = 0; i < height; i += NewGridElementConstants.DEFAULT_GRID_SIZE) {
			drawLine(backgroundContext, 0, i, width, i);
		}
	}

	private static void drawLine(Context2d context, int x, int y, int x2, int y2) {
		context.beginPath();
		context.moveTo(x + 0.5, y + 0.5); // +0.5 because a line of thickness 1.0 spans 50% left and 50% right (therefore it would not be on the 1 pixel - see https://developer.mozilla.org/en-US/docs/HTML/Canvas/Tutorial/Applying_styles_and_colors)
		context.lineTo(x2 + 0.5, y2 + 0.5);
		context.stroke();
	}
	
	public CanvasElement getCanvasElement() {
		return canvas.getCanvasElement();
	}
}
