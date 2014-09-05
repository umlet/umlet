package com.baselet.diagram.draw.swing;

import com.baselet.diagram.draw.DrawFunction;
import com.baselet.element.GridElement;

/**
 * Simple DrawHandler which doesn't really draw but can be used where a DrawHandler is expected (eg: height calculation of element for autoresize)
 */
public class PseudoDrawHandlerSwing extends DrawHandlerSwing {

	public PseudoDrawHandlerSwing(GridElement gridElement) {
		super(gridElement);
	}

	@Override
	protected void addDrawable(DrawFunction drawable) {
		/* do nothing */
	}
}
