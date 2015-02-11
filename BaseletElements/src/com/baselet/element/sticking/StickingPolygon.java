package com.baselet.element.sticking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;

public class StickingPolygon {

	public static class StickLine extends Line {

		StickLine(PointDouble p1, PointDouble p2) {
			super(p1, p2);
		}

		// calculates the difference between this line and the other line at the specified x or y coordinate (whichever fits better)
		public PointDouble diffToLine(StickLine s, int inX, int inY) {
			double x = 0;
			double y = 0;
			if (getEnd().x.equals(getStart().x)) {
				// AB: Fixed: use s.getStart().x instead of getStart().x
				x = s.getStart().x - (s.getEnd().x - s.getStart().x) - inX; // mitte der neuen linie

				if (s.getEnd().x.equals(s.getStart().x)) {
					// vertical lines - no y difference except the line is at an end
					y = 0;
					if (s.getStart().y > s.getEnd().y) {
						if (s.getStart().y < inY) {
							y = s.getStart().y - inY;
						}
						else if (s.getEnd().y > inY) {
							y = s.getEnd().y - inY;
						}
					}
					else {
						if (s.getEnd().y < inY) {
							y = s.getEnd().y - inY;
						}
						else if (s.getStart().y > inY) {
							y = s.getStart().y - inY;
						}
					}
					return new PointDouble(x, y);
				}
			}
			else {
				x = (inX - getStart().x) * (s.getEnd().x - s.getStart().x) / (getEnd().x - getStart().x) + s.getStart().x - inX;
			}

			if (getEnd().y.equals(getStart().y)) {
				// AB: Fixed: use s.getStart().x instead of getStart().x
				y = s.getStart().y - (s.getEnd().y - s.getStart().y) - inY;

				if (s.getEnd().y.equals(s.getStart().y)) {
					// horizontal lines - no x difference except the line is at an end
					x = 0;
					if (s.getStart().x > s.getEnd().x) {
						if (s.getStart().x < inX) {
							x = s.getStart().x - inX;
						}
						else if (s.getEnd().x > inX) {
							x = s.getEnd().x - inX;
						}
					}
					else {
						if (s.getEnd().x < inX) {
							x = s.getEnd().x - inX;
						}
						else if (s.getStart().x > inX) {
							x = s.getStart().x - inX;
						}
					}
				}
			}
			else {
				y = (inY - getStart().y) * (s.getEnd().y - s.getStart().y) / (getEnd().y - getStart().y) + s.getStart().y - inY;
			}

			return new PointDouble(x, y);
		}

		public boolean isConnected(PointDouble p, int maxDistance) {
			double distance = getDistanceToPoint(p);
			return distance < maxDistance;
		}
	}

	private final Vector<StickLine> stick = new Vector<StickLine>();
	private PointDouble lastpoint = null;
	private PointDouble firstpoint = null;
	private final int elementX;
	private final int elementY;

	// store all points for the copyZoomed() method
	private final List<PointDouble> allPoints = new ArrayList<PointDouble>();

	public StickingPolygon(int elementX, int elementY) {
		this.elementX = elementX;
		this.elementY = elementY;
	}

	public void addPoint(List<PointDouble> points) {
		for (PointDouble p : points) {
			addPoint(p.getX(), p.getY());
		}
	}

	public void addPoint(double x, double y) {
		PointDouble p = new PointDouble(elementX + x, elementY + y);
		allPoints.add(p);
		if (firstpoint == null) {
			firstpoint = p;
		}
		else {
			stick.add(new StickLine(lastpoint, p));
		}
		lastpoint = p;
	}

	public void addPoint(int x, int y, boolean connectToFirst) {
		this.addPoint(x, y);
		if (connectToFirst) {
			allPoints.add(firstpoint);
			stick.add(new StickLine(lastpoint, firstpoint));
		}
	}

	public void addRectangle(int x, int y, int width, int height) {
		addPoint(x, y);
		addPoint(x + width, y);
		addPoint(x + width, y + height);
		addPoint(x, y + height, true);
	}

	public void addRectangle(Rectangle rect) {
		addRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public StickLine getLine(int index) {
		return stick.get(index);
	}

	public Vector<StickLine> getStickLines() {
		return stick;
	}

	public int isConnected(PointDouble p, int gridSize) {
		int con = -1;
		for (int i = 0; i < stick.size(); i++) {
			if (stick.get(i).isConnected(p, gridSize)) {
				return i;
			}
		}

		return con;
	}

	@Override
	public String toString() {
		return "StickingPolygon [stick=" + Arrays.toString(stick.toArray(new StickLine[stick.size()])) + "]";
	}

}
