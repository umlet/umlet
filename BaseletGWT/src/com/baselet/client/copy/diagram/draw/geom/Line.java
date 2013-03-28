package com.baselet.client.copy.diagram.draw.geom;

public class Line {

	Point start;
	Point end;
	
	public Line() {
	}
	
	public Line(Point start, Point end) {
		super();
		this.start = start;
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}


	/**
	 * Checks the distance between this line and a specific point
	 * @param pointToCheck point to check the distance
	 * @return minimal distance from point to line as double value
	 */
	public double distance(Point pointToCheck) {
		return distanceHelper(start.x, start.y, end.x, end.y, pointToCheck.x, pointToCheck.y);
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
	
}
