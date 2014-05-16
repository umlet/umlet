package com.baselet.diagram.draw.geom;

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

}
