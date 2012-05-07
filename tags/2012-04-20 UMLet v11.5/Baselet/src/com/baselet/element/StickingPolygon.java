
package com.baselet.element;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.diagram.DrawPanel;
import com.umlet.element.Relation;
import com.umlet.element.relation.RelationLinePoint;


public class StickingPolygon {

	public class StickLine {

		private Point p1;
		private Point p2;

		// AB: important: tolerance <5 leads to zero tolerance at smaller zoom levels -> no sticking
		private int STICKING_TOLERANCE = 5;

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

		private void draw(Graphics2D g2) {
			g2.drawLine(p1.x, p1.y, p2.x, p2.y);
		}

		private boolean isConnected(Point p) {
			Line2D line = new Line2D.Double(p1, p2);
			double d = line.ptLineDist(p); // calculate distance

			// AB: original this tolerance was 4
			int delta = (int) (Main.getInstance().getDiagramHandler().getZoomFactor() * STICKING_TOLERANCE + 0.5);
			Polygon poly1 = new Polygon(); // create 2 rectangles with their size +/-4*zoom pixels
			poly1.addPoint(p1.x - delta, p2.y + delta);
			poly1.addPoint(p2.x + delta, p2.y + delta);
			poly1.addPoint(p2.x + delta, p1.y - delta);
			poly1.addPoint(p1.x - delta, p1.y - delta);
			Polygon poly2 = new Polygon();
			poly2.addPoint(p1.x + delta, p2.y - delta);
			poly2.addPoint(p2.x - delta, p2.y - delta);
			poly2.addPoint(p2.x - delta, p1.y + delta);
			poly2.addPoint(p1.x + delta, p1.y + delta);

			// AB: original this tolerance was 5
			return (d < delta) && ((poly1.contains(p) || (poly2.contains(p))));
			// inside maximum distance AND inside one of the rectangles
		}
	}

	private Vector<StickLine> stick = new Vector<StickLine>();
	private Point lastpoint = null;
	private Point firstpoint = null;

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

	public final void draw(Graphics2D g2) { // LME: draw the sticking polygon
		for (StickLine l : this.stick)
			l.draw(g2);
	}

	private int isConnected(Point p) {
		int con = -1;
		for (int i = 0; i < this.stick.size(); i++)
			if (this.stick.get(i).isConnected(p)) return i;

		return con;
	}

	public Vector<RelationLinePoint> getStickingRelationLinePoints(DrawPanel panel) {
		Vector<RelationLinePoint> lpts = new Vector<RelationLinePoint>();
		List<Relation> rels = panel.getAllRelations();
		for (Relation r : rels) {
			if (!r.isPartOfGroup()) {
				Point l1 = r.getAbsoluteCoorStart();
				Point l2 = r.getAbsoluteCoorEnd();
				int c1 = this.isConnected(l1);
				int c2 = this.isConnected(l2);
				if (c1 >= 0) lpts.add(new RelationLinePoint(r, 0, c1));
				if (c2 >= 0) lpts.add(new RelationLinePoint(r, r.getLinePoints().size() - 1, c2));
			}
		}
		return lpts;
	}

	/*
	 * FROM ENTITY
	 * public int doesCoordinateAppearToBeConnectedToMe(Point p) {
	 * //LME: switched to sticking polygon
	 * StickingPolygon sPoly = this.generateStickingBorder();
	 * if(sPoly != null) {
	 * Point[][] lines = sPoly.getLines();
	 * int aX=this.getX(), aY=this.getY(); //absolute xy coords
	 * Point p1,p2;
	 * for(int i=0;i<lines.length;i++) {
	 * p1=new Point(lines[i][0].x+aX,lines[i][0].y+aY);
	 * p2=new Point(lines[i][1].x+aX,lines[i][1].y+aY);
	 * if(isOnLine(p1,p2,p))
	 * return sPoly.getStickingInfo(new Point(lines[i][0].x,lines[i][0].y),new Point(lines[i][1].x,lines[i][1].y));
	 * }
	 * }
	 * return 0;
	 * }
	 * public Vector<RelationLinePoint> getAffectedRelationLinePoints(int action) {
	 * Vector<RelationLinePoint> ret=new Vector<RelationLinePoint>();
	 * Vector<Relation> allRelations=this.handler.getDrawPanel().getAllRelationsOnPanel();
	 * for (int i=0; i<allRelations.size(); i++) {
	 * Relation r=allRelations.elementAt(i);
	 * int tmpInt=r.getConnectedLinePoint(this, action);
	 * //if (tmpInt>0){
	 * //L.Trescher start
	 * if (tmpInt>0 && (!r.isPartOfGroup())){
	 * //L.Trescher end
	 * int first=tmpInt%1000-1;
	 * if (first>-1) {
	 * ret.add(new RelationLinePoint(r,first));
	 * }
	 * int last=tmpInt/1000-1;
	 * if (last>-1) {
	 * ret.add(new RelationLinePoint(r,last));
	 * }
	 * }
	 * }
	 * return ret;
	 * }
	 * -------------------------------------------
	 * FROM RELATION
	 * public int getConnectedLinePoint(Entity e, int action) {
	 * int ret1 = -1;
	 * int ret2 = -1;
	 * Point p = this.getStartPoint();
	 * Point abs = new Point((int) (this.getX() + p.getX()), (int) (this
	 * .getY() + p.getY()));
	 * int where = e.doesCoordinateAppearToBeConnectedToMe(abs);
	 * if ((where > 0) && ((where & action) > 0))
	 * ret1 = 0;
	 * p = this.getEndPoint();
	 * abs = new Point((int) (this.getX() + p.getX()), (int) (this.getY() + p
	 * .getY()));
	 * where = e.doesCoordinateAppearToBeConnectedToMe(abs);
	 * if ((where > 0) && ((where & action) > 0))
	 * ret2 = this.getLinePoints().size() - 1;
	 * // A.Mueller start
	 * if (clientServer != null && clientServer.equals("start"))
	 * ret1 = -1;
	 * if (clientServer != null && clientServer.equals("end"))
	 * ret2 = -1;
	 * // A.Mueller end
	 * ret1++;
	 * ret2++;
	 * return ret1 + 1000 * ret2;
	 * }
	 */
}
