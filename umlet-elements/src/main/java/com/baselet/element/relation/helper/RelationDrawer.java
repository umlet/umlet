package com.baselet.element.relation.helper;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.basics.geom.GeometricFunctions;
import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.diagram.draw.helper.ThemeFactory;
import com.baselet.element.sticking.PointDoubleIndexed;

public class RelationDrawer {

	static final double SHORTEN_IF_ARROW = 1; // issue 270: if an arrow end is attached to a line, the line is shorter by this value to avoid the line going through the arrow-end
	static final double ARROWEND_DISPLACEMENT = 0.5; // issue 270: the arrow end is placed slightly displaced to the inside to avoid going through elemens which they point at

	public static final double ARROW_LENGTH = RelationPointConstants.POINT_SELECTION_RADIUS * 1.3;
	private static final double DIAGONAL_CROSS_LENGTH = RelationPointConstants.POINT_SELECTION_RADIUS * 0.9;
	private static final double BOX_SIZE = 20;

	public static final double SMALL_CIRCLE_RADIUS = RelationPointConstants.POINT_SELECTION_RADIUS / 2;
	public static final double MIDDLE_CIRCLE_RADIUS = RelationPointConstants.POINT_SELECTION_RADIUS;
	public static final double LARGE_CIRCLE_RADIUS = MIDDLE_CIRCLE_RADIUS * 3;

	public static Rectangle drawBoxArrow(DrawHandler drawer, Line line, boolean drawOnStart, String matchedText, ResizableObject resizableObject) {
		double oldFontsize = drawer.getFontSize();
		drawer.setFontSize(12);

		double height = BOX_SIZE;
		double distance = drawer.getDistanceBorderToText();
		double width = Math.max(BOX_SIZE, drawer.textWidth(matchedText) + distance * 2);
		PointDoubleIndexed point = (PointDoubleIndexed) line.getPoint(drawOnStart);
		Rectangle r = new Rectangle(point.getX() - width / 2, point.getY() - height / 2, width, height);
		drawer.drawRectangle(r);

		int arrow = 4;
		ColorOwn oldBgColor = drawer.getBackgroundColor();
		drawer.setBackgroundColor(drawer.getForegroundColor());
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
		NORMAL, CLOSED, DIAMOND, MEASURE, SINGLE_PIPE, DOUBLE_PIPE
	}

	public static void drawArrowToLine(DrawHandler drawer, Line line, boolean drawOnStart, ArrowEndType arrowEndType, boolean fillBody, boolean invertArrow) {
		drawArrowToLine(line.getPointOnLineWithDistanceFrom(drawOnStart, ARROWEND_DISPLACEMENT), drawer, line, drawOnStart, arrowEndType, fillBody, invertArrow);
	}

	public static void drawArrowToLine(PointDouble point, DrawHandler drawer, Line line, boolean drawOnStart, ArrowEndType arrowEndType, boolean fillBody, boolean invertArrow) {
		if (invertArrow) {
			point = line.getPointOnLineWithDistanceFrom(drawOnStart, ARROW_LENGTH);
			drawOnStart = !drawOnStart;
		}
		int arrowAngle = drawOnStart ? 150 : 30;
		PointDouble p1 = calcPointArrow(point, line.getAngleOfSlope() - arrowAngle);
		PointDouble p2 = calcPointArrow(point, line.getAngleOfSlope() + arrowAngle);
		List<PointDouble> points = new ArrayList<PointDouble>();

		if (arrowEndType == ArrowEndType.MEASURE || arrowEndType == ArrowEndType.SINGLE_PIPE || arrowEndType == ArrowEndType.DOUBLE_PIPE) {
			PointDouble m1 = calcPoint(point, line.getAngleOfSlope() + 90, SharedConstants.DEFAULT_GRID_SIZE);
			PointDouble m2 = calcPoint(point, line.getAngleOfSlope() - 90, SharedConstants.DEFAULT_GRID_SIZE);
			points.add(m1);
			points.add(m2);
			points.add(point);
		}
		if (arrowEndType == ArrowEndType.DOUBLE_PIPE) {
			PointDouble p = line.getPointOnLineWithDistanceFrom(!drawOnStart, ARROW_LENGTH * 1.5);
			PointDouble m1 = calcPoint(p, line.getAngleOfSlope() + 90, SharedConstants.DEFAULT_GRID_SIZE);
			PointDouble m2 = calcPoint(p, line.getAngleOfSlope() - 90, SharedConstants.DEFAULT_GRID_SIZE);
			points.add(p);
			points.add(m1);
			points.add(m2);
			points.add(p);
			points.add(point);
		}

		if (arrowEndType != ArrowEndType.SINGLE_PIPE && arrowEndType != ArrowEndType.DOUBLE_PIPE) {
			points.add(p1);
			points.add(point);
			points.add(p2);

			if (arrowEndType == ArrowEndType.CLOSED) {
				points.add(p1);
			}
			else if (arrowEndType == ArrowEndType.DIAMOND) {
				double lengthDiamond = GeometricFunctions.getDistanceBetweenLineAndPoint(p1, p2, point) * 2;
				PointDouble pDiamond = drawOnStart ? line.getPointOnLineWithDistanceFrom(true, lengthDiamond) : line.getPointOnLineWithDistanceFrom(false, lengthDiamond);
				points.add(pDiamond);
				points.add(p1);
			}
		}

		if (fillBody) {
			ColorOwn bgColor = drawer.getBackgroundColor();
			drawer.setBackgroundColor(drawer.getForegroundColor());
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
		double circleRadius = openDirection == null ? MIDDLE_CIRCLE_RADIUS : LARGE_CIRCLE_RADIUS;
		drawCircle(line.getPoint(drawOnStart), circleRadius, drawer, line, drawOnStart, resizableObject, openDirection, drawCross);
	}

	public static void drawCircle(PointDouble point, double circleRadius, DrawHandler drawer, Line line, boolean drawOnStart, ResizableObject resizableObject, Direction openDirection, boolean drawCross) {
		if (openDirection == null) { // full circle
			drawer.drawCircle(point.getX(), point.getY(), circleRadius);
		}
		else if (point instanceof PointDoubleIndexed && (openDirection == Direction.LEFT || openDirection == Direction.RIGHT)) { // interface half circle
			PointDoubleIndexed pointIndex = (PointDoubleIndexed) point;

			ColorOwn bg = drawer.getBackgroundColor();
			drawer.setBackgroundColor(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.TRANSPARENT));

			Direction directionOfCircle = line.getDirectionOfLine(drawOnStart);
			if (directionOfCircle == Direction.RIGHT) {
				drawer.drawArc(point.getX(), point.getY() - circleRadius / 2, circleRadius, circleRadius, 90, 180, true);
				resizableObject.setPointMinSize(pointIndex.getIndex(), new Rectangle(-circleRadius / 4, -circleRadius / 2, circleRadius * 0.75, circleRadius));
			}
			else if (directionOfCircle == Direction.DOWN) {
				drawer.drawArc(point.getX() - circleRadius / 2, point.getY(), circleRadius, circleRadius, 0, 180, true);
				resizableObject.setPointMinSize(pointIndex.getIndex(), new Rectangle(-circleRadius / 2, -circleRadius / 4, circleRadius, circleRadius * 0.75));
			}
			else if (directionOfCircle == Direction.LEFT) {
				drawer.drawArc(point.getX() - circleRadius, point.getY() - circleRadius / 2, circleRadius, circleRadius, -90, 180, true);
				resizableObject.setPointMinSize(pointIndex.getIndex(), new Rectangle(-circleRadius / 2, -circleRadius / 2, circleRadius * 0.75, circleRadius));
			}
			else {
				drawer.drawArc(point.getX() - circleRadius / 2, point.getY() - circleRadius, circleRadius, circleRadius, -180, 180, true);
				resizableObject.setPointMinSize(pointIndex.getIndex(), new Rectangle(-circleRadius / 2, -circleRadius / 2, circleRadius, circleRadius * 0.75));
			}

			drawer.setBackgroundColor(bg);
		}
		if (drawCross) {
			double length = RelationPointConstants.POINT_SELECTION_RADIUS / 2;
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
