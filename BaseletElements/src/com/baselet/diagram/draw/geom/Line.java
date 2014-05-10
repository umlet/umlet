package com.baselet.diagram.draw.geom;

import org.apache.log4j.Logger;

public class Line {

	private static final Logger log = Logger.getLogger(Line.class);

	PointDouble start;
	PointDouble end;

	public Line() {}

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
		return GeometricFunctions.distanceBetweenTwoPoints(start, end);
	}

	public double getAngleOfSlope() {
		double radius = getLength();
		double rad = Math.acos((start.x - end.x) / radius);
		double radDeg = Math.toDegrees(rad);
		if (start.y < end.y) {
			radDeg = 360 - radDeg;
		}
		return radDeg;
	}

	/**
	 * Checks the distance between this line and a specific point
	 * @param pointToCheck point to check the distance
	 * @return minimal distance from point to line as double value
	 */
	public double getDistanceToPoint(PointDouble pointToCheck) {
		double dist = GeometricFunctions.getDistanceBetweenLineAndPoint(start, end, pointToCheck);
		log.trace("Minimal distance between " + this + " and " + pointToCheck + " is " + dist);
		return dist;
	}

	public boolean equalsContent(Line other) {
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		}
		else if (!end.equals(other.end)) {
			return false;
		}
		if (start == null) {
			if (other.start != null) {
				return false;
			}
		}
		else if (!start.equals(other.start)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Line [start=" + start + ", end=" + end + "]";
	}

	public PointDouble[] toPoints() {
		return new PointDouble[] { start, end };
	}

}
