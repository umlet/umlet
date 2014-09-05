package com.baselet.elementnew.facet.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.GeometricFunctions;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.elementnew.element.uml.relation.PointDoubleIndexed;
import com.baselet.elementnew.element.uml.relation.RelationPointHandler;
import com.baselet.elementnew.element.uml.relation.ResizableObject;

public class RelationDrawer {

	private static final double ARROW_LENGTH = RelationPointHandler.POINT_SELECTION_RADIUS * 1.3;
	private static final double DIAGONAL_CROSS_LENGTH = RelationPointHandler.POINT_SELECTION_RADIUS * 0.9;
	private static final double BOX_SIZE = 20;

	public static Rectangle drawBoxArrow(DrawHandler drawer, Line line, boolean drawOnStart, String matchedText, ResizableObject resizableObject) {
		double oldFontsize = drawer.getStyle().getFontSize();
		drawer.setFontSize(12);

		double height = BOX_SIZE;
		double distance = drawer.getDistanceBorderToText();
		double width = Math.max(BOX_SIZE, drawer.textWidth(matchedText) + distance * 2);
		PointDoubleIndexed point = (PointDoubleIndexed) line.getPoint(drawOnStart);
		Rectangle r = new Rectangle(point.getX() - width / 2, point.getY() - height / 2, width, height);
		drawer.drawRectangle(r);

		int arrow = 4;
		ColorOwn oldBgColor = drawer.getStyle().getBackgroundColor();
		drawer.setBackgroundColor(drawer.getStyle().getForegroundColor());
		if (matchedText.equals("^")) {
			PointDouble start = new PointDouble(point.getX(), point.getY() - arrow);
			drawer.drawLines(start, new PointDouble(point.getX() + arrow, point.getY() + arrow), new PointDouble(point.getX() - arrow, point.getY() + arrow), start);
		}
		else if (matchedText.equals("<")) {
			PointDouble start = new PointDouble(point.getX() - arrow, point.getY());
			drawer.drawLines(start, new PointDouble(point.getX() + arrow, point.getY() - arrow), new PointDouble(point.getX() + arrow, point.getY() + arrow), start);
		}
		else if (matchedText.equals(">")) {
			PointDouble start = new PointDouble(point.getX() + arrow, point.getY());
			drawer.drawLines(start, new PointDouble(point.getX() - arrow, point.getY() - arrow), new PointDouble(point.getX() - arrow, point.getY() + arrow), start);
		}
		else if (matchedText.equals("v")) {
			PointDouble start = new PointDouble(point.getX() - arrow, point.getY() - arrow);
			drawer.drawLines(start, new PointDouble(point.getX() + arrow, point.getY() - arrow), new PointDouble(point.getX(), point.getY() + arrow), start);
		}
		else if (matchedText.equals("=")) {
			int dist = 2;
			int size = 6;
			drawer.drawLines(new PointDouble(point.getX() - size, point.getY() - dist), new PointDouble(point.getX() + size, point.getY() - dist), new PointDouble(point.getX(), point.getY() - size));
			drawer.drawLines(new PointDouble(point.getX() + size, point.getY() + dist), new PointDouble(point.getX() - size, point.getY() + dist), new PointDouble(point.getX(), point.getY() + size));
		}
		else {
			drawer.print(matchedText, new PointDouble(point.getX() - width / 2 + distance, point.getY() + drawer.textHeightMax() / 2), AlignHorizontal.LEFT);
			resizableObject.setPointMinSize(point.getIndex(), new Rectangle(-width / 2, -height / 2, width, height));
		}
		drawer.setFontSize(oldFontsize);
		drawer.setBackgroundColor(oldBgColor);
		return r;
	}

	public static enum ArrowEndType {
		NORMAL, CLOSED, DIAMOND
	}

	public static void drawArrowToLine(DrawHandler drawer, Line line, boolean drawOnStart, ArrowEndType arrowEndType, boolean fillBody, boolean invertArrow) {
		drawArrowToLine(line.getPoint(drawOnStart), drawer, line, drawOnStart, arrowEndType, fillBody, invertArrow);
	}

	public static void drawArrowToLine(PointDouble point, DrawHandler drawer, Line line, boolean drawOnStart, ArrowEndType arrowEndType, boolean fillBody, boolean invertArrow) {
		if (invertArrow) {
			point = line.getPointOnLineWithDistanceFrom(drawOnStart, ARROW_LENGTH);
			drawOnStart = !drawOnStart;
		}
		int arrowAngle = drawOnStart ? 150 : 30;
		PointDouble p1 = calcPointArrow(point, line.getAngleOfSlope() - arrowAngle);
		PointDouble p2 = calcPointArrow(point, line.getAngleOfSlope() + arrowAngle);
		List<PointDouble> points = new ArrayList<PointDouble>(Arrays.asList(p1, point, p2));
		if (arrowEndType == ArrowEndType.CLOSED) {
			points.add(p1);
		}
		else if (arrowEndType == ArrowEndType.DIAMOND) {
			double lengthDiamond = GeometricFunctions.getDistanceBetweenLineAndPoint(p1, p2, point) * 2;
			PointDouble pDiamond = drawOnStart ? line.getPointOnLineWithDistanceFrom(true, lengthDiamond) : line.getPointOnLineWithDistanceFrom(false, lengthDiamond);
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

	private static PointDouble calcPointArrow(PointDouble point, double angleTotal) {
		return calcPoint(point, angleTotal, ARROW_LENGTH);
	}

	private static PointDouble calcPointCross(PointDouble point, double angleTotal) {
		return calcPoint(point, angleTotal, DIAGONAL_CROSS_LENGTH);
	}

	private static PointDouble calcPoint(PointDouble point, double angleTotal, double length) {
		double x = point.x + length * Math.cos(Math.toRadians(angleTotal));
		double y = point.y + length * Math.sin(Math.toRadians(angleTotal));
		return new PointDouble(x, y);
	}

	public static void drawCircle(DrawHandler drawer, Line line, boolean drawOnStart, ResizableObject resizableObject, Direction openDirection, boolean drawCross) {
		PointDoubleIndexed point = (PointDoubleIndexed) line.getPoint(drawOnStart);
		if (openDirection == null) { // full circle
			drawer.drawCircle(point.getX(), point.getY(), RelationPointHandler.POINT_SELECTION_RADIUS);
		}
		else if (openDirection == Direction.LEFT || openDirection == Direction.RIGHT) { // interface half circle
			ColorOwn bg = drawer.getStyle().getBackgroundColor();
			drawer.setBackgroundColor(ColorOwn.TRANSPARENT);

			double circleRadius = RelationPointHandler.POINT_SELECTION_RADIUS * 3;
			Direction directionOfCircle = line.getDirectionOfLine(drawOnStart);
			if (directionOfCircle == Direction.RIGHT) {
				drawer.drawArc(point.getX(), point.getY() - circleRadius / 2, circleRadius, circleRadius, 90, 180, true);
				resizableObject.setPointMinSize(point.getIndex(), new Rectangle(-circleRadius / 4, -circleRadius / 2, circleRadius * 0.75, circleRadius));
			}
			else if (directionOfCircle == Direction.DOWN) {
				drawer.drawArc(point.getX() - circleRadius / 2, point.getY(), circleRadius, circleRadius, 0, 180, true);
				resizableObject.setPointMinSize(point.getIndex(), new Rectangle(-circleRadius / 2, -circleRadius / 4, circleRadius, circleRadius * 0.75));
			}
			else if (directionOfCircle == Direction.LEFT) {
				drawer.drawArc(point.getX() - circleRadius, point.getY() - circleRadius / 2, circleRadius, circleRadius, -90, 180, true);
				resizableObject.setPointMinSize(point.getIndex(), new Rectangle(-circleRadius / 2, -circleRadius / 2, circleRadius * 0.75, circleRadius));
			}
			else {
				drawer.drawArc(point.getX() - circleRadius / 2, point.getY() - circleRadius, circleRadius, circleRadius, -180, 180, true);
				resizableObject.setPointMinSize(point.getIndex(), new Rectangle(-circleRadius / 2, -circleRadius / 2, circleRadius, circleRadius * 0.75));
			}

			drawer.setBackgroundColor(bg);
		}
		if (drawCross) {
			double length = RelationPointHandler.POINT_SELECTION_RADIUS / 2;
			drawer.drawLine(point.getX() - length, point.getY(), point.getX() + length, point.getY());
			drawer.drawLine(point.getX(), point.getY() - length, point.getX(), point.getY() + length);
		}
	}

	public static void drawDiagonalCross(DrawHandler drawer, Line line, boolean drawOnStart, ResizableObject resizableObject, Direction openDirection, boolean drawCross) {
		PointDouble p = line.getPointOnLineWithDistanceFrom(drawOnStart, ARROW_LENGTH);
		drawer.drawLines(calcPointCross(p, line.getAngleOfSlope() + 45), calcPointCross(p, line.getAngleOfSlope() - 135));
		drawer.drawLines(calcPointCross(p, line.getAngleOfSlope() - 45), calcPointCross(p, line.getAngleOfSlope() + 135));
	}
}
