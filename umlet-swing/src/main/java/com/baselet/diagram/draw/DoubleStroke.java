package com.baselet.diagram.draw;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

public class DoubleStroke implements Stroke {
	private Stroke outerStroke;
	private Stroke innerStroke;

	public DoubleStroke(float lineWidth, float distanceBetweenLines, int cap, int join, float miterlimit, float[] dash, float dash_phase) {
		outerStroke = new BasicStroke(lineWidth + distanceBetweenLines, cap, join, miterlimit, dash, dash_phase);
		innerStroke = new BasicStroke(lineWidth, cap, join, miterlimit, dash, dash_phase);
	}

	@Override
	public Shape createStrokedShape(Shape s) {
		Shape outline = outerStroke.createStrokedShape(s);
		return innerStroke.createStrokedShape(outline);
	}
}
