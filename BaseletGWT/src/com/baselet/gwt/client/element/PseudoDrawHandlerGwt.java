package com.baselet.gwt.client.element;

import com.baselet.diagram.draw.DrawFunction;
import com.google.gwt.canvas.client.Canvas;

/**
 * Simple DrawHandler which doesn't really draw but can be used where a DrawHandler is expected (eg: height calculation of element for autoresize)
 */
public class PseudoDrawHandlerGwt extends DrawHandlerGwt {

	public PseudoDrawHandlerGwt(Canvas canvas) {
		super(canvas);
	}

	@Override
	protected void addDrawable(DrawFunction drawable) {
		/* do nothing */
	}
}
