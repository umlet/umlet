package com.baselet.control;

import com.baselet.element.Point;

public class MathUtils {


	/**
	 * Checks the distance between a line and a specific point
	 * @param linePoint1 startpoint of line
	 * @param linePoint2 endpoint of line
	 * @param pointToCheck point to check the distance
	 * @return minimal distance from point to line as double value
	 */
	public static double distance(Point linePoint1, Point linePoint2, Point pointToCheck) {
		return distanceHelper(linePoint1.x, linePoint1.y, linePoint2.x, linePoint2.y, pointToCheck.x, pointToCheck.y);
	}

	/**
	 * implementation is based on http://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment/2233538#2233538
	 */
	private static double distanceHelper(int x1, int y1, int x2, int y2, int checkX, int checkY) {
		int px = x2 - x1;
		int py = y2 - y1;

		float mult = px * px + py * py;
		double u = ((checkX - x1) * px + (checkY - y1) * py) / mult;

		if (u > 1) u = 1;
		else if (u < 0) u = 0;

		double x = x1 + u * px;
		double y = y1 + u * py;

		double dx = x - checkX;
		double dy = y - checkY;

		double dist = Math.sqrt(dx * dx + dy * dy);

		return dist;
	}

}
