package com.baselet.diagram.draw.geom;

import org.apache.log4j.Logger;

public class Line {
	
	private static final Logger log = Logger.getLogger(Line.class);

	PointDouble start;
	PointDouble end;
	
	public Line() {
	}
	
	public Line(PointDouble start, PointDouble end) {
		super();
		this.start = start;
		this.end = end;
	}

	public PointDouble getStart() {
		return start;
	}

	public void setStart(PointDouble start) {
		this.start = start;
	}

	public PointDouble getEnd() {
		return end;
	}

	public void setEnd(PointDouble end) {
		this.end = end;
	}
	
	public PointDouble getCenter() {
	    return new PointDouble((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
	}
	
	public double getLength() {
		return distanceBetweenTwoPoints(start, end);
	}

	public double getAngleOfSlope() {
		double radius = getLength();
		double rad = Math.acos((start.x-end.x)/radius);
		double radDeg = Math.toDegrees(rad);
		if (start.y < end.y){
			radDeg = 360- radDeg;
		}
		return radDeg;
	}

	/**
	 * Checks the distance between this line and a specific point
	 * @param pointToCheck point to check the distance
	 * @return minimal distance from point to line as double value
	 */
	public double getDistanceToPoint(PointDouble pointToCheck) {
		double dist = distanceHelper(start.x, start.y, end.x, end.y, pointToCheck.x, pointToCheck.y);
		log.trace("Minimal distance between " + this + " and " + pointToCheck + " is " + dist);
		return dist;
	}

	/**
	 * implementation is based on http://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment/2233538#2233538
	 */
	private static double distanceHelper(double x1, double y1, double x2, double y2, double checkX, double checkY) {
		double px = x2 - x1;
		double py = y2 - y1;

		double mult = px * px + py * py;
		double u = ((checkX - x1) * px + (checkY - y1) * py) / mult;

		if (u > 1) u = 1;
		else if (u < 0) u = 0;

		double x = x1 + u * px;
		double y = y1 + u * py;
		
		return distanceBetweenTwoPoints(x, y, checkX, checkY);
	}

	public static double distanceBetweenTwoPoints(PointDouble p1, PointDouble p2) {
		return distanceBetweenTwoPoints(p1.x,  p1.y, p2.x, p2.y);
	}
	
	public static double distanceBetweenTwoPoints(double x1, double y1, double x2, double y2) {
		double xDist = x1-x2;
		double yDist = y1-y2;
		return Math.sqrt(xDist * xDist + yDist * yDist);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Line other = (Line) obj;
		if (end == null) {
			if (other.end != null) return false;
		}
		else if (!end.equals(other.end)) return false;
		if (start == null) {
			if (other.start != null) return false;
		}
		else if (!start.equals(other.start)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Line [start=" + start + ", end=" + end + "]";
	}

	public PointDouble[] toPoints() {
		return new PointDouble[] {start, end};
	}
	
}
