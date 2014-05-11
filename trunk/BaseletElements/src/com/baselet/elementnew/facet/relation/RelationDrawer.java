package com.baselet.elementnew.facet.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.elementnew.element.uml.relation.RelationPoints;

public class RelationDrawer {

	public static void drawBoxArrow(DrawHandler drawer, Line line, boolean drawOnStart) {
		int size = 20;
		PointDouble point = drawOnStart ? line.getStart() : line.getEnd();
		System.out.println("DRAW " + point);
		drawer.drawRectangle(point.getX() - size / 2, point.getY() - size / 2, size, size);
	}

	public static void drawArrowToLine(DrawHandler drawer, Line line, boolean drawOnStart, boolean inverseArrow, boolean closeArrow) {
		PointDouble point = drawOnStart ? line.getStart() : line.getEnd();
		double angleOfSlopeOfLine = line.getAngleOfSlope();
		if (inverseArrow) {
			drawOnStart = !drawOnStart;
		}
		int angle = drawOnStart ? 150 : 30;
		PointDouble p1 = calcPoint(point, angleOfSlopeOfLine, true, angle);
		PointDouble p2 = calcPoint(point, angleOfSlopeOfLine, false, angle);
		List<PointDouble> points = new ArrayList<PointDouble>(Arrays.asList(p1, point, p2));
		if (closeArrow) {
			points.add(p1);
		}
		drawer.drawLines(points);
	}

	private static PointDouble calcPoint(PointDouble point, double angleOfSlopeOfLine, boolean first, int angle) {
		int arrowLength = RelationPoints.POINT_SELECTION_RADIUS;
		int arrowAngle = angle;
		double angleTotal = first ? angleOfSlopeOfLine - arrowAngle : angleOfSlopeOfLine + arrowAngle;
		double x = point.x + arrowLength * Math.cos(Math.toRadians(angleTotal));
		double y = point.y + arrowLength * Math.sin(Math.toRadians(angleTotal));
		return new PointDouble(x, y);
	}
}
