
package com.baselet.element.sticking;

import java.util.Arrays;
import java.util.Vector;

import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;


public class StickingPolygon {

	public class StickLine extends Line {

		private StickLine(PointDouble p1, PointDouble p2) {
			super(p1, p2);
		}

		// calculates the difference between this line and the other line at the specified x or y coordinate (whichever fits better)
		public PointDouble diffToLine(StickLine s, int x, int y) {
			PointDouble diff = new PointDouble(0, 0);
			if (getEnd().x == getStart().x) {
				// AB: Fixed: use s.getStart().x instead of getStart().x
				diff.x = s.getStart().x - (s.getEnd().x - s.getStart().x) - x; // mitte der neuen linie

				if (s.getEnd().x == s.getStart().x) {
					// vertical lines - no y difference except the line is at an end
					diff.y = 0;
					if (s.getStart().y > s.getEnd().y) {
						if (s.getStart().y < y) diff.y = s.getStart().y - y;
						else if (s.getEnd().y > y) diff.y = s.getEnd().y - y;
					}
					else {
						if (s.getEnd().y < y) diff.y = s.getEnd().y - y;
						else if (s.getStart().y > y) diff.y = s.getStart().y - y;
					}
					return diff;
				}
			}
			else diff.x = (x - getStart().x) * (s.getEnd().x - s.getStart().x) / (getEnd().x - getStart().x) + s.getStart().x - x;

			if (getEnd().y == getStart().y) {
				// AB: Fixed: use s.getStart().x instead of getStart().x
				diff.y = s.getStart().y - (s.getEnd().y - s.getStart().y) - y;

				if (s.getEnd().y == s.getStart().y) {
					// horizontal lines - no x difference except the line is at an end
					diff.x = 0;
					if (s.getStart().x > s.getEnd().x) {
						if (s.getStart().x < x) diff.x = s.getStart().x - x;
						else if (s.getEnd().x > x) diff.x = s.getEnd().x - x;
					}
					else {
						if (s.getEnd().x < x) diff.x = s.getEnd().x - x;
						else if (s.getStart().x > x) diff.x = s.getStart().x - x;
					}
				}
			}
			else diff.y = (y - getStart().y) * (s.getEnd().y - s.getStart().y) / (getEnd().y - getStart().y) + s.getStart().y - y;

			return diff;
		}

		public boolean isConnected(PointDouble p, int maxDistance) {
			double distance = this.getDistanceToPoint(p);
			return (distance < maxDistance);
		}
	}

	private Vector<StickLine> stick = new Vector<StickLine>();
	private PointDouble lastpoint = null;
	private PointDouble firstpoint = null;
	private int elementX;
	private int elementY;

	public StickingPolygon(int elementX, int elementY) {
		this.elementX = elementX;
		this.elementY = elementY;
	}

	@Deprecated
	public void addPoint(Point p) {
		addPoint(p.x, p.y);
	}

	@Deprecated
	public void addPoint(Point p, boolean connect_to_first) {
		addPoint(p.x, p.y, connect_to_first);
	}

	public void addPoint(int x, int y) {
		PointDouble p = new PointDouble(elementX + x, elementY + y);
		if (firstpoint == null) {
			firstpoint = p;
		} else {
			stick.add(new StickLine(lastpoint, p));
		}
		lastpoint = p;
	}
	public void addPoint(int x, int y, boolean connectToFirst) {
		this.addPoint(x, y);
		if (connectToFirst) {
			stick.add(new StickLine(lastpoint, firstpoint));
		}
	}

	public void addRectangle(int x, int y, int width, int height) {
		addPoint(x, y);
		addPoint(x+width, y);
		addPoint(x+width, y+height);
		addPoint(x, y+height, true);
	}

	public void addRectangle(Rectangle rect) {
		addRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public StickLine getLine(int index) {
		return this.stick.get(index);
	}

	public Vector<StickLine> getStickLines() {
		return this.stick;
	}

	public int isConnected(PointDouble p, int gridSize) {
		int con = -1;
		for (int i = 0; i < this.stick.size(); i++)
			if (this.stick.get(i).isConnected(p, gridSize)) return i;

		return con;
	}

	@Override
	public String toString() {
		return "StickingPolygon [stick=" + Arrays.toString(stick.toArray(new StickLine[stick.size()])) + "]";
	}
	
}
