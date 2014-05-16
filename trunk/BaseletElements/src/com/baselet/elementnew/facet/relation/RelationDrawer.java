package com.baselet.elementnew.facet.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.GeometricFunctions;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.elementnew.element.uml.relation.RelationPoints;

public class RelationDrawer {

	private static final double ARROW_LENGTH = RelationPoints.POINT_SELECTION_RADIUS * 1.3;

	public static void drawBoxArrowEquals(DrawHandler drawer, Line line, boolean drawOnStart) {
		PointDouble point = drawBox(drawer, line, drawOnStart);

		int dist = 2;
		int size = 6;
		drawer.drawLines(new PointDouble(point.getX() - size, point.getY() - dist), new PointDouble(point.getX() + size, point.getY() - dist), new PointDouble(point.getX(), point.getY() - size));
		drawer.drawLines(new PointDouble(point.getX() + size, point.getY() + dist), new PointDouble(point.getX() - size, point.getY() + dist), new PointDouble(point.getX(), point.getY() + size));
	}

	public static void drawBoxArrow(DrawHandler drawer, Line line, boolean drawOnStart, Direction arrowDirection) {
		PointDouble point = drawBox(drawer, line, drawOnStart);

		int arrow = 4;
		ColorOwn bgColorOld = drawer.getStyle().getBackgroundColor();
		drawer.setBackgroundColor(drawer.getStyle().getForegroundColor());
		if (arrowDirection == Direction.UP) {
			PointDouble start = new PointDouble(point.getX(), point.getY() - arrow);
			drawer.drawLines(start, new PointDouble(point.getX() + arrow, point.getY() + arrow), new PointDouble(point.getX() - arrow, point.getY() + arrow), start);
		}
		else if (arrowDirection == Direction.LEFT) {
			PointDouble start = new PointDouble(point.getX() - arrow, point.getY());
			drawer.drawLines(start, new PointDouble(point.getX() + arrow, point.getY() - arrow), new PointDouble(point.getX() + arrow, point.getY() + arrow), start);
		}
		else if (arrowDirection == Direction.RIGHT) {
			PointDouble start = new PointDouble(point.getX() + arrow, point.getY());
			drawer.drawLines(start, new PointDouble(point.getX() - arrow, point.getY() - arrow), new PointDouble(point.getX() - arrow, point.getY() + arrow), start);
		}
		else if (arrowDirection == Direction.DOWN) {
			PointDouble start = new PointDouble(point.getX() - arrow, point.getY() - arrow);
			drawer.drawLines(start, new PointDouble(point.getX() + arrow, point.getY() - arrow), new PointDouble(point.getX(), point.getY() + arrow), start);
		}
		drawer.setBackgroundColor(bgColorOld);

	}

	private static PointDouble drawBox(DrawHandler drawer, Line line, boolean drawOnStart) {
		int box = 20;
		PointDouble point = drawOnStart ? line.getStart() : line.getEnd();
		drawer.drawRectangle(point.getX() - box / 2, point.getY() - box / 2, box, box);
		return point;
	}

	public static void drawArrowToLine(DrawHandler drawer, Line line, boolean drawOnStart, boolean inverseArrow, boolean closeArrow, boolean diamond, boolean fillBody) {
		PointDouble point = drawOnStart ? line.getStart() : line.getEnd();
		double angleOfSlopeOfLine = line.getAngleOfSlope();
		if (inverseArrow) {
			drawOnStart = !drawOnStart;
		}
		int arrowAngle = drawOnStart ? 150 : 30;
		PointDouble p1 = calcPoint(point, angleOfSlopeOfLine - arrowAngle);
		PointDouble p2 = calcPoint(point, angleOfSlopeOfLine + arrowAngle);
		List<PointDouble> points = new ArrayList<PointDouble>(Arrays.asList(p1, point, p2));
		if (closeArrow) {
			points.add(p1);
		}
		else if (diamond) {
			double lengthDiamond = GeometricFunctions.getDistanceBetweenLineAndPoint(p1, p2, point) * 2;
			PointDouble pDiamond = drawOnStart ? line.getPointOnLineWithDistanceFromStart(lengthDiamond) : line.getPointOnLineWithDistanceFromEnd(lengthDiamond);
			points.add(pDiamond);
			points.add(p1);
		}

		if (fillBody) {
			ColorOwn bgColor = drawer.getStyle().getBackgroundColor();
			drawer.setBackgroundColor(drawer.getStyle().getForegroundColor());
			drawer.drawLines(points);
			drawer.setBackgroundColor(bgColor);
		}
		else {
			drawer.drawLines(points);
		}
	}

	private static PointDouble calcPoint(PointDouble point, double angleTotal) {
		double x = point.x + ARROW_LENGTH * Math.cos(Math.toRadians(angleTotal));
		double y = point.y + ARROW_LENGTH * Math.sin(Math.toRadians(angleTotal));
		return new PointDouble(x, y);
	}
}
