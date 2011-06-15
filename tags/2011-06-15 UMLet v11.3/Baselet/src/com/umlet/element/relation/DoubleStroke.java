package com.umlet.element.relation;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

public class DoubleStroke implements Stroke {
	private Stroke outerStroke;
	private Stroke innerStroke;

	public DoubleStroke(int lineWidth, int distanceBetweenLines, int cap, int join, float miterlimit, float[] dash, float dash_phase) {
		this.outerStroke = new BasicStroke(lineWidth + distanceBetweenLines, cap, join, miterlimit, dash, dash_phase);
		this.innerStroke = new BasicStroke(lineWidth, cap, join, miterlimit, dash, dash_phase);
	}

	@Override
	public Shape createStrokedShape(Shape s) {
		Shape outline = outerStroke.createStrokedShape(s);
		return innerStroke.createStrokedShape(outline);
	}
}
