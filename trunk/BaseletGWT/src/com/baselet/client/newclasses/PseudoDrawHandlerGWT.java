package com.baselet.client.newclasses;

import com.baselet.client.copy.diagram.draw.DrawFunction;
import com.google.gwt.canvas.client.Canvas;

public class PseudoDrawHandlerGWT extends DrawHandlerGWT {

	public PseudoDrawHandlerGWT(Canvas canvas) {
		super(canvas);
	}

	@Override
	protected void addDrawable(DrawFunction drawable) {
		/*do nothing*/
	}
}
