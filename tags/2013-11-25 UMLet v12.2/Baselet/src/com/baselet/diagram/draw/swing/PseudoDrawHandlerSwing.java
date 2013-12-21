package com.baselet.diagram.draw.swing;

import com.baselet.diagram.draw.DrawFunction;



/**
 * Simple DrawHandler which doesn't really draw but can be used where a DrawHandler is expected (eg: height calculation of element for autoresize)
 */
public class PseudoDrawHandlerSwing extends BaseDrawHandlerSwing {
	
	@Override
	protected void addDrawable(DrawFunction drawable) {
		/*do nothing*/
	}
}
