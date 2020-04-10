package com.baselet.control.basics.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeometricFunctions {

	public static double distanceBetweenTwoPoints(PointDouble p1, PointDouble p2) {
		return GeometricFunctions.distanceBetweenTwoPoints(p1.x, p1.y, p2.x, p2.y);
	}

	public static double getDistanceBetweenLineAndPoint(PointDouble start, PointDouble end, PointDouble pointToCheck) {
		return getDistanceBetweenLineAndPoint(start.x, start.y, end.x, end.y, pointToCheck.x, pointToCheck.y);
	}

	private static double distanceBetweenTwoPoints(double x1, double y1, double x2, double y2) {
		double xDist = x1 - x2;
		double yDist = y1 - y2;
		return Math.sqrt(xDist * xDist + yDist * yDist);
	}

	/**
	 * implementation is based on http://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment/2233538#2233538
	 */
	private static double getDistanceBetweenLineAndPoint(double x1, double y1, double x2, double y2, double checkX, double checkY) {
		double px = x2 - x1;
		double py = y2 - y1;

		double mult = px * px + py * py;
		double u = ((checkX - x1) * px + (checkY - y1) * py) / mult;

		if (u > 1) {
			u = 1;
		}
		else if (u < 0) {
			u = 0;
		}

		double x = x1 + u * px;
		double y = y1 + u * py;

		return GeometricFunctions.distanceBetweenTwoPoints(x, y, checkX, checkY);
	}

	/**
	 * for math information see http://www.mathisfunforum.com/viewtopic.php?id=9657
	 */
	public static PointDouble getPointOnLineWithDistanceFromStart(PointDouble start, PointDouble end, double distance) {
		double xDiff = end.getX() - start.getX();
		double yDiff = end.getY() - start.getY();
		double length = distanceBetweenTwoPoints(start, end);
		double distanceToGo = distance / length;
		return new PointDouble(start.getX() + xDiff * distanceToGo, start.getY() + yDiff * distanceToGo);
	}

	/**
	 * from https://stackoverflow.com/questions/15594424/line-crosses-rectangle-how-to-find-the-cross-points/15594751#15594751
	 */
	public static PointDouble getIntersectionPoint(Line lineA, Line lineB) {
		double x1 = lineA.getStart().getX();
		double y1 = lineA.getStart().getY();
		double x2 = lineA.getEnd().getX();
		double y2 = lineA.getEnd().getY();

		double x3 = lineB.getStart().getX();
		double y3 = lineB.getStart().getY();
		double x4 = lineB.getEnd().getX();
		double y4 = lineB.getEnd().getY();

		PointDouble p = null;

		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (d != 0) {
			double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
			double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

			p = new PointDouble(xi, yi);

			// remove intersections which are not on the line (use 1 instead of 0 distance for tolerance)
			if (lineA.getDistanceToPoint(p) > 1 || lineB.getDistanceToPoint(p) > 1) {
				p = null;
			}
		}

		return p;
	}

	public static List<PointDouble> getIntersectionPoints(Line line, Rectangle rectangle) {
		List<PointDouble> list = Arrays.asList(
				// TOP
				getIntersectionPoint(line,
						new Line(new PointDouble(rectangle.getX(), rectangle.getY()),
								new PointDouble(rectangle.getX() + rectangle.getWidth(), rectangle.getY()))),
				// BOTTOM
				getIntersectionPoint(line,
						new Line(new PointDouble(rectangle.getX(), rectangle.getY() + rectangle.getHeight()),
								new PointDouble(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()))),
				// LEFT
				getIntersectionPoint(line,
						new Line(new PointDouble(rectangle.getX(), rectangle.getY()),
								new PointDouble(rectangle.getX(), rectangle.getY() + rectangle.getHeight()))),
				// RIGHT
				getIntersectionPoint(line,
						new Line(new PointDouble(rectangle.getX() + rectangle.getWidth(), rectangle.getY()),
								new PointDouble(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()))));

		// remove nulls
		List<PointDouble> returnList = new ArrayList<PointDouble>();
		for (PointDouble p : list) {
			if (p != null) {
				returnList.add(p);
			}
		}
		return returnList;
	}
}
