package com.baselet.diagram.draw;

import java.awt.Shape;

import com.baselet.diagram.DiagramHandler;

/**
 * Simple DrawHandler which doesn't really draw but can be used where a DrawHandler is expected (eg: height calculation of element for autoresize)
 */
public class PseudoDrawHandler extends BaseDrawHandler {
	
	protected void addShape(Shape s) {
		/*ignore*/
	}

	protected void addText(Text t) {
		/*ignore*/
	}
}
