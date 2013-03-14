
package com.baselet.element;

import java.util.Collection;
import java.util.Vector;

import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.umlet.element.Relation;
import com.umlet.element.relation.RelationLinePoint;


public class StickingPolygon {

	public class StickLine {

		private Point p1;
		private Point p2;

		private StickLine(Point p1, Point p2) {
			this.p1 = p1;
			this.p2 = p2;
		}

		// calculates the difference between this line and the other line at the specified x or y coordinate (whichever fits better)
		public Point diffToLine(StickLine s, int x, int y) {
			Point diff = new Point(0, 0);
			if (p2.x == p1.x) {
				// AB: Fixed: use s.p1.x instead of p1.x
				diff.x = s.p1.x - (s.p2.x - s.p1.x) - x; // mitte der neuen linie

				if (s.p2.x == s.p1.x) {
					// vertical lines - no y difference except the line is at an end
					diff.y = 0;
					if (s.p1.y > s.p2.y) {
						if (s.p1.y < y) diff.y = s.p1.y - y;
						else if (s.p2.y > y) diff.y = s.p2.y - y;
					}
					else {
						if (s.p2.y < y) diff.y = s.p2.y - y;
						else if (s.p1.y > y) diff.y = s.p1.y - y;
					}
					return diff;
				}
			}
			else diff.x = (x - p1.x) * (s.p2.x - s.p1.x) / (p2.x - p1.x) + s.p1.x - x;

			if (p2.y == p1.y) {
				// AB: Fixed: use s.p1.x instead of p1.x
				diff.y = s.p1.y - (s.p2.y - s.p1.y) - y;

				if (s.p2.y == s.p1.y) {
					// horizontal lines - no x difference except the line is at an end
					diff.x = 0;
					if (s.p1.x > s.p2.x) {
						if (s.p1.x < x) diff.x = s.p1.x - x;
						else if (s.p2.x > x) diff.x = s.p2.x - x;
					}
					else {
						if (s.p2.x < x) diff.x = s.p2.x - x;
						else if (s.p1.x > x) diff.x = s.p1.x - x;
					}
				}
			}
			else diff.y = (y - p1.y) * (s.p2.y - s.p1.y) / (p2.y - p1.y) + s.p1.y - y;

			return diff;
		}

		public Point getP1() {
			return p1;
		}
		
		public Point getP2() {
			return p2;
		}


		private boolean isConnected(Point p, int gridSize) {
			double distance = Utils.distance(p1, p2, p);
			return (distance < gridSize);
		}
	}

	private Vector<StickLine> stick = new Vector<StickLine>();
	private Point lastpoint = null;
	private Point firstpoint = null;

	public void addPoint(int x, int y) {
		addPoint(new Point(x, y));
	}

	public void addPoint(int x, int y, boolean connect_to_first) {
		addPoint(new Point(x, y), connect_to_first);
	}
	
	public void addPoint(Point p) {
		// add a line with corresponding stickingInfo
		if (this.lastpoint != null) {
			stick.add(new StickLine(lastpoint, p));
		}
		else this.firstpoint = p;
		this.lastpoint = p;
	}

	public StickLine getLine(int index) {
		return this.stick.get(index);
	}

	public void addPoint(Point p, boolean connect_to_first) {
		this.addPoint(p);
		if (connect_to_first && (this.firstpoint != null)) this.addPoint(this.firstpoint);
	}

	public void addLine(Point p1, Point p2) {
		stick.add(new StickLine(p1, p2));
	}

	public Vector<StickLine> getStickLines() {
		return this.stick;
	}

	private int isConnected(Point p, int gridSize) {
		int con = -1;
		for (int i = 0; i < this.stick.size(); i++)
			if (this.stick.get(i).isConnected(p, gridSize)) return i;

		return con;
	}

	public Vector<RelationLinePoint> getStickingRelationLinePoints(DiagramHandler handler) {
		Vector<RelationLinePoint> lpts = new Vector<RelationLinePoint>();
		Collection<Relation> rels = handler.getDrawPanel().getAllRelations();
		for (Relation r : rels) {
			if (!r.isPartOfGroup()) {
				Point l1 = r.getAbsoluteCoorStart();
				Point l2 = r.getAbsoluteCoorEnd();
				int c1 = this.isConnected(l1, handler.getGridSize());
				int c2 = this.isConnected(l2, handler.getGridSize());
				if (c1 >= 0) lpts.add(new RelationLinePoint(r, 0, c1));
				if (c2 >= 0) lpts.add(new RelationLinePoint(r, r.getLinePoints().size() - 1, c2));
			}
		}
		return lpts;
	}
}
