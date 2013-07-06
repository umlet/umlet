package com.umlet.element.experimental.uml.relation;

import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;

public class Arrow {

	static void drawArrowToLine(BaseDrawHandler drawer, Line line, boolean arrowOnLineStart) {
		Point point = arrowOnLineStart ? line.getStart() : line.getEnd();
		double angleOfSlopeOfLine = line.getAngleOfSlope();
		int angle = arrowOnLineStart ? 150 : 30;
		drawArrowLine(drawer, point, angleOfSlopeOfLine, true, angle);
		drawArrowLine(drawer, point, angleOfSlopeOfLine, false, angle);
	}

	static void drawArrowLine(BaseDrawHandler drawer, Point start, double angleOfSlopeOfLine, boolean first, int angle) {
		int arrowLength = RelationPoints.POINT_SELECTION_RADIUS;
		int arrowAngle = angle;
		double angleTotal = first ? angleOfSlopeOfLine-arrowAngle : angleOfSlopeOfLine+arrowAngle;
		double xx = start.x + arrowLength * Math.cos(Math.toRadians(angleTotal));
		double yx = start.y + arrowLength * Math.sin(Math.toRadians(angleTotal));
		drawer.drawLine(start.x, start.y, (float)xx, (float)yx);
	}
}
