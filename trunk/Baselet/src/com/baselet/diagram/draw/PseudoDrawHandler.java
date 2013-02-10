package com.baselet.diagram.draw;

import java.awt.Shape;

/**
 * Simple DrawHandler which doesn't really draw but can be used where a DrawHandler is expected (eg: height calculation of element for autoresize)
 */
public class PseudoDrawHandler extends BaseDrawHandler {
	
	@Override
	protected void addShape(Shape s) {
		/*ignore*/
	}

	@Override
	protected void addText(Text t) {
		/*ignore*/
	}
}
