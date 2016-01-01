package com.baselet.control.basics.geom;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.enums.Direction;

public class Line {

	private static final Logger log = LoggerFactory.getLogger(Line.class);

	private final PointDouble start;
	private final PointDouble end;

	public Line(PointDouble start, PointDouble end) {
		super();
		this.start = start;
		this.end = end;
	}

	public PointDouble getStart() {
		return start;
	}

	public PointDouble getEnd() {
		return end;
	}

	public PointDouble getPoint(boolean start) {
		return start ? getStart() : getEnd();
	}

	public PointDouble getCenter() {
		return new PointDouble((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
	}

	public double getLength() {
		return GeometricFunctions.distanceBetweenTwoPoints(start, end);
	}

	public PointDouble getPointOnLineWithDistanceFrom(boolean fromStart, double distance) {
		if (fromStart) {
			return GeometricFunctions.getPointOnLineWithDistanceFromStart(start, end, distance);
		}
		else { // from end
			return GeometricFunctions.getPointOnLineWithDistanceFromStart(end, start, distance);
		}
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

	public PointDouble[] toPoints() {
		return new PointDouble[] { start, end };
	}

	public Direction getDirectionOfLine(boolean directionOfStart) {
		double angleOfSlope = getAngleOfSlope();
		Direction direction;
		if (angleOfSlope > 315 || angleOfSlope < 45) {
			direction = Direction.RIGHT;
		}
		else if (angleOfSlope < 135) {
			direction = Direction.DOWN;
		}
		else if (angleOfSlope < 225) {
			direction = Direction.LEFT;
		}
		else {
			direction = Direction.UP;
		}
		if (!directionOfStart) {
			direction = direction.invert();
		}
		return direction;
	}

	public List<PointDouble> getIntersectionPoints(Rectangle rectangle) {
		return GeometricFunctions.getIntersectionPoints(this, rectangle);
	}

	public Line getShorterVersion(boolean fromStart, double shortenBy) {
		PointDouble shortenedPoint = getPointOnLineWithDistanceFrom(fromStart, shortenBy);
		if (fromStart) {
			return new Line(shortenedPoint, end);
		}
		else {
			return new Line(start, shortenedPoint);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (end == null ? 0 : end.hashCode());
		result = prime * result + (start == null ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Line other = (Line) obj;
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

}
