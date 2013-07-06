package com.umlet.element.experimental.uml.relation;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;

public class ArrowStart extends Arrow {

	public static void draw(BaseDrawHandler drawer, Line line) {
		drawArrowToLine(drawer, line, true);
	}
}