package com.baselet.element.old.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.enums.LineType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.diagram.draw.helper.ThemeFactory;
import com.baselet.element.Selector;
import com.baselet.element.facet.common.LayerFacet;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.old.relation.Arrow;
import com.baselet.element.old.relation.EmptyShape;
import com.baselet.element.old.relation.Multiplicity;
import com.baselet.element.old.relation.NoShape;
import com.baselet.element.old.relation.Port;
import com.baselet.element.old.relation.Qualifier;
import com.baselet.element.old.relation.Role;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.polygon.NoStickingPolygonGenerator;

@SuppressWarnings("serial")
public class Relation extends OldGridElement {

	String beginQualifier;
	String endQualifier;
	String beginArrow;
	String endArrow;
	String beginMultiplicity;
	String endMultiplicity;
	String beginRole;
	String endRole;
	String lineType;
	String eerRelDir;

	Vector<String> _strings;

	// A.Mueller start
	String clientServer;
	// A.Mueller end

	// G.Mueller start
	String beginPort;
	String endPort;
	String middleArrow;
	String csdStartText; // Arrow-Text for composite structure diagram
	String csdEndText;

	// G.Mueller end

	private final float SELECTBOXSIZE = 12;
	private final float SELECTCIRCLESIZE = 15;

	private Vector<String> getStrings() {
		if (_strings == null) {
			_strings = new Vector<String>();
		}
		return _strings;
	}

	private void setStrings(Vector<String> v) {
		_strings = v;
	}

	private Point getCenterOfLine() {
		Point ret = new Point();
		if (getLinePoints().size() % 2 == 1) {
			ret = getLinePoints().elementAt(getLinePoints().size() / 2);
		}
		else {
			Point p1 = getLinePoints().elementAt(getLinePoints().size() / 2);
			Point p2 = getLinePoints().elementAt(getLinePoints().size() / 2 - 1);
			ret.x = (p1.x + p2.x) / 2;
			ret.y = (p1.y + p2.y) / 2;
		}
		return ret;
	}

	@Override
	public String getAdditionalAttributes() {
		Vector<String> tmp = new Vector<String>();
		// tmp.add(beginQualifier);
		// tmp.add(endQualifier);
		// tmp.add(beginArrow);
		// tmp.add(endArrow);
		// tmp.add(beginMultiplicity);
		// tmp.add(endMultiplicity);
		// tmp.add(beginRole);
		// tmp.add(endRole);
		// tmp.add(lineType);

		/* tmp.add(""+this.getX()); tmp.add(""+this.getY()); tmp.add(""+this.getWidth()); tmp.add(""+this.getHeight()); */

		for (int i = 0; i < getLinePoints().size(); i++) {
			Point p = getLinePoints().elementAt(i);
			String s1 = "" + p.x;
			String s2 = "" + p.y;
			tmp.add(s1);
			tmp.add(s2);
		}

		String ret = Utils.composeStrings(tmp, Constants.DELIMITER_ADDITIONAL_ATTRIBUTES);
		return ret;
	}

	@Override
	public void setAdditionalAttributes(String s) {
		getLinePoints().clear();
		Vector<String> tmp = Utils.decomposeStringsIncludingEmptyStrings(s, Constants.DELIMITER_ADDITIONAL_ATTRIBUTES);
		for (int i = 0; i < tmp.size(); i = i + 2) {
			int x = Integer.parseInt(tmp.elementAt(i));
			int y = Integer.parseInt(tmp.elementAt(i + 1));
			getLinePoints().add(new Point(x, y));
		}
	}

	@Override
	public void setPanelAttributes(String state) {
		beginQualifier = "";
		endQualifier = "";
		beginArrow = "";
		endArrow = "";
		beginMultiplicity = "";
		endMultiplicity = "";
		beginRole = "";
		endRole = "";
		lineType = "-";
		eerRelDir = "";
		// G.Mueller.Start
		middleArrow = "";
		beginPort = "";
		endPort = "";
		// G.Mueller. End
		setStrings(null);

		super.setPanelAttributes(state);

		Vector<String> tmp = Utils.decomposeStrings(state);

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.startsWith("q1=") & s.length() > 3) {
				beginQualifier = s.substring(3, s.length());
			}
			else if (s.startsWith("q2=") & s.length() > 3) {
				endQualifier = s.substring(3, s.length());
			}
			else if (s.startsWith("m1=") & s.length() > 3) {
				beginMultiplicity = s.substring(3, s.length());
			}
			else if (s.startsWith("m2=") & s.length() > 3) {
				endMultiplicity = s.substring(3, s.length());
			}
			else if (s.startsWith("r1=") & s.length() > 3) {
				beginRole = s.substring(3, s.length());
			}
			else if (s.startsWith("r2=") & s.length() > 3) {
				endRole = s.substring(3, s.length());
			}
			else if (s.startsWith("p1=") & s.length() > 3) {
				beginPort = s.substring(3, s.length());
			}
			else if (s.startsWith("p2=") & s.length() > 3) {
				endPort = s.substring(3, s.length());
			}
			else if (s.startsWith("lt=") & s.length() > 3) {

				csdStartText = "";
				csdEndText = "";

				// ***

				if (s.indexOf("<[") >= 0) {
					beginArrow = "compStart";
					if (s.length() > 6) {
						csdStartText = getCSDText(s)[0];
						s = s.replace("<[" + csdStartText + "]", "<[]");
					}
				}

				if (s.indexOf("]>") >= 0) {
					endArrow = "compEnd";
					if (s.length() > 6) {
						csdEndText = getCSDText(s)[1];
						s = s.replace("[" + csdEndText + "]>", "[]>");
					}
				}

				if (s.indexOf("]<") >= 0) {
					beginArrow = beginArrow + "del";
				}

				if (s.indexOf(">[") >= 0) {
					endArrow = endArrow + "del";
				}

				if (s.indexOf(">>>>>") >= 0) {
					endArrow = "<<<";
				}
				else if (s.indexOf(">>>>") >= 0) {
					endArrow = "X";
				}
				else if (s.indexOf(">>>") >= 0) {
					endArrow = "x";
				}
				else if (s.indexOf(">>") >= 0) {
					endArrow = "<<";
				}
				else if (s.indexOf("m>") >= 0) {
					endArrow = "crowsFoot";
				}
				else if (s.indexOf(">") >= 0) {
					if (endArrow.equals("")) {
						endArrow = "<";
					}
				}

				if (s.indexOf("<<<<<") >= 0) {
					beginArrow = "<<<";
				}
				else if (s.indexOf("<<<<") >= 0) {
					beginArrow = "X";
				}
				else if (s.indexOf("<<<") >= 0) {
					beginArrow = "x";
				}
				else if (s.indexOf("<<") >= 0) {
					beginArrow = "<<";
				}
				else if (s.indexOf("<m") >= 0) {
					beginArrow = "crowsFoot";
				}
				else if (s.indexOf("<") >= 0) {
					if (beginArrow.equals("")) {
						beginArrow = "<";
					}
				}

				if (s.indexOf("<EER>") >= 0) {
					beginArrow = "";
					endArrow = "";
					eerRelDir = "EER1";
				}
				else if (s.indexOf("<EER") >= 0) {
					beginArrow = "";
					endArrow = "";
					eerRelDir = "EER2";
				}
				else if (s.indexOf("EER>") >= 0) {
					beginArrow = "";
					endArrow = "";
					eerRelDir = "EER3";
				}
				else if (s.indexOf("EER") >= 0) {
					beginArrow = "";
					endArrow = "";
					eerRelDir = "EER_SUBCLASS";
				}

				// A.Mueller Beginn
				clientServer = "";

				// ***

				if (s.indexOf("(()") >= 0) {
					// beginArrow = ""; G.Mueller
					clientServer = "provideRequire";
				}
				else if (s.indexOf("())") >= 0) {
					// endArrow = ""; G.Mueller
					clientServer = "requireProvide";
				}

				if (s.indexOf("<(+)") >= 0) {
					beginArrow = "packageStart";
					clientServer = " ";
				}
				else if (s.indexOf("<()") >= 0) {
					clientServer = "start"; // used for setting the startpoint
					// nonstickable
					beginArrow = "require";
				}
				else if (s.indexOf("<(") >= 0) {
					clientServer = "start"; // used for setting the
					// startpoint
					// not stickable
					beginArrow = "provide";

				}
				else if (s.indexOf("<x") >= 0) {
					beginArrow = "n";
				}

				if (s.indexOf("(+)>") >= 0) {
					endArrow = "packageEnd";
					clientServer = " ";
				}
				else if (s.indexOf("()>") >= 0) {
					clientServer = "end"; // used for setting the endpoint
					// nonstickable
					endArrow = "require";
				}
				else if (s.indexOf(")>") >= 0) {
					clientServer = "end"; // used for setting the endpoint
					// nonstickable
					endArrow = "provide";
				}
				else if (s.indexOf("x>") >= 0) {
					endArrow = "n";
				}
				// A.Mueller End
				// Mueller G. End

				// Mueller G. Start

				if (s.indexOf(">()") >= 0 && clientServer.equals("")) {
					middleArrow = "delegationArrowRight";
					if (endArrow.equals("<")) {
						endArrow = "";
					}
				}
				else if (s.indexOf("()<") >= 0 && clientServer.equals("")) {
					middleArrow = "delegationArrowLeft";
					if (beginArrow.equals("<")) {
						beginArrow = "";
					}
				}
				else if (s.indexOf("()") >= 0 && clientServer.equals("")) {
					middleArrow = "delegation";
				}
				else if (s.indexOf("(") >= 0 && clientServer.equals("")) {
					middleArrow = "delegationStart";
					lineType = "-.";
				}
				else if (s.indexOf(")") >= 0 && clientServer.equals("")) {
					middleArrow = "delegationEnd";
					lineType = ".-";
				}
				// G.Mueller: LineTyp check here:

				if (s.indexOf(".") >= 0 & s.indexOf("-") >= s.indexOf(".")) {
					lineType = ".-";
				}
				else if (s.indexOf("-") >= 0 & s.indexOf(".") >= s.indexOf("-")) {
					lineType = "-.";
				}
				else if (s.indexOf(LineType.DOTTED.getValue()) >= 0) {
					lineType = LineType.DOTTED.getValue();
				}
				else if (s.indexOf(LineType.DASHED.getValue()) >= 0) {
					lineType = LineType.DASHED.getValue();
				}
				else if (s.indexOf(LineType.SOLID.getValue()) >= 0) {
					lineType = LineType.SOLID.getValue();
				}
				else if (s.substring(3, s.length()).indexOf(LineType.DOUBLE.getValue()) >= 0) {
					lineType = LineType.DOUBLE.getValue();
				}
				else if (s.indexOf(LineType.DOUBLE_DOTTED.getValue()) >= 0) {
					lineType = LineType.DOUBLE_DOTTED.getValue();
				}
				else if (s.indexOf(LineType.DOUBLE_DASHED.getValue()) >= 0) {
					lineType = LineType.DOUBLE_DASHED.getValue();
				}
			}
			else {
				getStrings().add(s);
			}
		}
	} // Created objects have no sideeffects

	// Only exception: no point is outside shape
	// At least 2 points must be provided
	public static Vector<Point> getIntersectingLineSegment(Area r, Vector<Point> points) {
		Vector<Point> ret = new Vector<Point>();

		// If no segment found, take last two points
		Point pp_end = points.elementAt(points.size() - 1);
		Point pp_start = points.elementAt(points.size() - 2);

		for (int i = 1; i < points.size(); i++) {
			pp_end = points.elementAt(i);
			if (!r.contains(Converter.convert(pp_end))) {
				// End point of intersecting line found
				pp_start = points.elementAt(i - 1);

				ret.add(pp_start);
				ret.add(pp_end);
				return ret;
			}
		}

		ret.add(pp_start);
		ret.add(pp_end);
		return ret;
	}

	public static Point moveNextTo(Area rFixed, Rectangle rMovable, Point pStart, Point pEnd) {
		// These ints can simply be added to line
		int centerDiffX = -rMovable.getWidth() / 2;
		int centerDiffY = -rMovable.getHeight() / 2;

		int vectorX = pEnd.x - pStart.x;
		int vectorY = pEnd.y - pStart.y;

		int startx = pStart.x;
		int starty = pStart.y;
		int endx = pEnd.x;
		int endy = pEnd.y;

		for (@SuppressWarnings("unused")
		int i = 0;; i++) {
			endx += vectorX;
			endy += vectorY;
			rMovable.setLocation(endx + centerDiffX, endy + centerDiffY);
			if (!rFixed.intersects(Converter.convert(rMovable))) {
				break;
			}
		}

		int newx = 0;
		int newy = 0;
		for (int i = 0; i < 10; i++) {
			newx = (endx + startx) / 2;
			newy = (endy + starty) / 2;
			rMovable.setLocation(newx + centerDiffX, newy + centerDiffY);
			if (rFixed.intersects(Converter.convert(rMovable))) {
				startx = newx;
				starty = newy;
			}
			else {
				endx = newx;
				endy = newy;
			}
		}

		Point ret = new Point(newx + centerDiffX, newy + centerDiffY);
		return ret;
	}

	public boolean lineUp(Vector<Rectangle> shapes, Vector<Point> points, int hotspotx, int hotspoty) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		// Remove point with the same coordinates
		for (int i = points.size() - 1; i > 0; i--) {
			Point p1 = points.elementAt(i);
			Point p2 = points.elementAt(i - 1);
			if (p1.x == p2.x & p1.y == p2.y) {
				points.removeElementAt(i);
			}
		}
		if (points.size() <= 1) {
			return false;
		}

		if (shapes.size() <= 1) {
			return true;
		}

		// Vector ret=new Vector();

		// Rectangle rFixed;
		Rectangle rMovable;
		Area tmpArea = new Area();
		for (int i = 0; i < shapes.size() - 1; i++) {
			Rectangle r = shapes.elementAt(i);
			if (i == 0) { // The hotspot of the first element is set
				Point p = points.elementAt(0);
				r.setLocation(p.x - hotspotx, p.y - hotspoty);
			}
			Area a = new Area(Converter.convert(r));
			tmpArea.add(a);

			// rFixed=(Rectangle)shapes.elementAt(i);
			rMovable = shapes.elementAt(i + 1);

			/* if (i==0) { // The hotspot of the first element is set Point p=(Point)points.elementAt(0); rFixed.setLocation(p.x-hotspotx,p.y-hotspoty); } */

			Vector<Point> tmp = getIntersectingLineSegment(tmpArea, points);
			Point startIntersectingLine = tmp.elementAt(0);
			Point endIntersectingLine = tmp.elementAt(1);

			Point res = moveNextTo(tmpArea, rMovable, startIntersectingLine, endIntersectingLine);
			// ret.add(res);

			if (rMovable instanceof Arrow) {
				Arrow arrow = (Arrow) rMovable;

				Point diffA = new Point(-startIntersectingLine.x + endIntersectingLine.x, -startIntersectingLine.y + endIntersectingLine.y);
				Point diffB1 = new Point(diffA.y, -diffA.x);
				Point diffB2 = new Point(-diffB1.x, -diffB1.y);
				Point a1 = new Point(2 * diffA.x + diffB1.x, 2 * diffA.y + diffB1.y);
				Point a2 = new Point(2 * diffA.x + diffB2.x, 2 * diffA.y + diffB2.y);

				a1 = Utils.normalize(a1, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
				a2 = Utils.normalize(a2, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());

				arrow.setArrowEndA(a1);
				arrow.setArrowEndB(a2);

				// A.Mueller start
				if (arrow.getString().equals("n")) {
					// this is pretty much the same as above, but it
					// was hard to work out what it does so here it
					// is repeated with better names and some
					// comments. I only made the original vector longer and
					// increased the pixelsize(fontsize)
					// to get the point further towards the center.
					Point start = startIntersectingLine;
					Point end = endIntersectingLine;
					// vectorA is the vector between the two points which is the
					// line between the points...
					Point vectorA = new Point(-start.x + end.x, -start.y + end.y);
					// vector down is a vector standing 90 degrees on the line,
					// vector up is the same in the opposite direction..
					Point vectorDown = new Point(vectorA.y, -vectorA.x);
					Point vectorUp = new Point(-vectorDown.x, -vectorDown.y);
					Point newA1 = new Point(4 * vectorA.x + vectorDown.x, 4 * vectorA.y + vectorDown.y);
					Point newA2 = new Point(4 * vectorA.x + vectorUp.x, 4 * vectorA.y + diffB2.y);

					// this calculates the proportion of the two dimensions of
					// the point compared to each other
					// (which means propX + propY = 1) and multiplies it with
					// the second parameter...
					newA1 = Utils.normalize(newA1, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 2);
					newA2 = Utils.normalize(newA2, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 2);

					arrow.setCrossEndA(newA1);
					arrow.setCrossEndB(newA2);
				}
				else if (arrow.getString().equals("require")) {
					int size = (int) (20 * zoom);
					Point start = startIntersectingLine;
					Point end = endIntersectingLine;
					Point upperLeft = new Point();
					Point bottomDown = new Point();
					if (start.getX() > end.getX()) {
						upperLeft = new Point(0, -size / 2);
						bottomDown = new Point(size, size / 2);

					}
					else if (start.getX() < end.getX()) {
						upperLeft = new Point(-size, -size / 2);
						bottomDown = new Point(0, size / 2);
					}
					else if (start.getX() == end.getX()) {
						if (start.getY() < end.getY()) {
							upperLeft = new Point(-size / 2, -size);
							bottomDown = new Point(size / 2, 0);
						}
						else if (start.getY() > end.getY()) {
							upperLeft = new Point(-size / 2, 0);
							bottomDown = new Point(size / 2, size);
						}
					}
					arrow.setCrossEndA(upperLeft);
					arrow.setCrossEndB(bottomDown);

				}
				else if (arrow.getString().equals("provide")) {
					int size = (int) (30 * zoom);
					Point start = startIntersectingLine;
					Point end = endIntersectingLine;
					Point upperLeft = new Point();
					Point bottomDown = new Point();
					if (start.getX() > end.getX()) {
						upperLeft = new Point(0, -size / 2);
						bottomDown = new Point(size, size / 2);
						arrow.setArcStart(90);
						arrow.setArcEnd(180);

					}
					else if (start.getX() < end.getX()) {
						upperLeft = new Point(-size, -size / 2);
						bottomDown = new Point(0, size / 2);
						arrow.setArcStart(90);
						arrow.setArcEnd(-180);
					}
					else if (start.getX() == end.getX()) {
						if (start.getY() < end.getY()) {
							upperLeft = new Point(-size / 2, -size);
							bottomDown = new Point(size / 2, 0);
							arrow.setArcStart(0);
							arrow.setArcEnd(-180);
						}
						else if (start.getY() > end.getY()) {
							upperLeft = new Point(-size / 2, 0);
							bottomDown = new Point(size / 2, size);
							arrow.setArcStart(0);
							arrow.setArcEnd(180);
						}
					}
					arrow.setCrossEndA(upperLeft);
					arrow.setCrossEndB(bottomDown);

					// A.Mueller end
				}

			}

			// ATTENTION: this Recangle will become the rFixed in the next loop
			rMovable.setLocation(res.x, res.y);
		}

		return true;
	}

	/**
	 * A relation is only in range (= in the selection frame) if every relation point is in the selection frame
	 */
	@Override
	public boolean isInRange(Rectangle rect1) {
		// Assume that the rect contains all relation points
		for (Point p : getLinePoints()) {
			// We must add the displacement from the top left corner of the drawpanel to the point coordinates
			Point realPoint = new Point(p.getX() + getRectangle().x, p.getY() + getRectangle().y);
			// If only one point is not in the selection rectangle, the method returns false
			if (!rect1.contains(realPoint)) {
				return false;
			}
		}
		return true;
	}

	public boolean isOnLine(int i) {
		if (i - 1 >= 0 & i + 1 < getLinePoints().size()) {
			Point x1 = getLinePoints().elementAt(i - 1);
			Point x2 = getLinePoints().elementAt(i + 1);
			Point p = getLinePoints().elementAt(i);
			if (p.distance(x1) + p.distance(x2) < x1.distance(x2) + 5) {
				return true;
			}
		}
		return false;
	}

	public int getWhereToInsert(Point p) {
		for (int i = 0; i < getLinePoints().size() - 1; i++) {
			Point x1 = getLinePoints().elementAt(i);
			Point x2 = getLinePoints().elementAt(i + 1);
			if (p.distance(x1) + p.distance(x2) < x1.distance(x2) + 5) {
				return i + 1;
			}
		}
		return -1;
	}

	public int getLinePoint(Point p) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		for (int i = 0; i < getLinePoints().size(); i++) {
			Point x = getLinePoints().elementAt(i);
			if (p.distance(x) < SELECTCIRCLESIZE * zoom) {
				return i;
			}
		}
		return -1;
	}

	private <T> Vector<T> flipVector(Vector<T> v) {
		Vector<T> ret = new Vector<T>();
		for (int i = v.size() - 1; i >= 0; i--) {
			ret.add(v.elementAt(i));
		}
		return ret;
	}

	@Override
	public boolean contains(java.awt.Point p) {
		// other relations which are selected are prioritized
		for (GridElement other : HandlerElementMap.getHandlerForElement(this).getDrawPanel().getGridElements()) {
			Selector s = HandlerElementMap.getHandlerForElement(other).getDrawPanel().getSelector();
			if (other != this && other instanceof Relation && s.isSelected(other)) {
				int xDist = getRectangle().x - other.getRectangle().x;
				int yDist = getRectangle().y - other.getRectangle().y;
				Point modifiedP = new Point(p.x + xDist, p.y + yDist); // the point must be modified, because the other relation has other coordinates
				boolean containsHelper = ((Relation) other).calcContains(modifiedP);
				if (s.isSelected(other) && containsHelper) {
					return false;
				}
			}
		}
		return calcContains(Converter.convert(p));
	}

	private boolean calcContains(Point p) {
		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		for (int i = 0; i < getLinePoints().size(); i++) {
			Point x = getLinePoints().elementAt(i);
			if (p.distance(x) < SELECTCIRCLESIZE * zoom) {
				return true;
			}
		}

		for (int i = 0; i < getLinePoints().size() - 1; i++) {
			Point x1 = getLinePoints().elementAt(i);
			Point x2 = getLinePoints().elementAt(i + 1);

			if (p.distance(x1) + p.distance(x2) > x1.distance(x2) + 5) {
				continue;
			}

			// system origin translated to x1
			double p1x = x2.getX() - x1.getX();
			double p1y = x2.getY() - x1.getY();
			double p2x = p.getX() - x1.getX();
			double p2y = p.getY() - x1.getY();
			// constant - calculated constant by rotating line + calculation intersection point
			double c = (p1x * p2x + p1y * p2y) / (p1x * p1x + p1y * p1y);

			// intersection point
			double i1x = p1x * c;
			double i1y = p1y * c;

			// abstand
			double ax = i1x - p2x;
			double ay = i1y - p2y;
			double a = Math.sqrt(ax * ax + ay * ay);

			if (a < 5) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contains(int x, int y) {
		return contains(new java.awt.Point(x, y));
	}

	private Vector<Point> _points;

	public Vector<Point> getLinePoints() {
		if (_points == null) {
			_points = new Vector<Point>();
		}
		return _points;
	}

	public void moveLinePoint(int index, int diffx, int diffy) {
		Point p = getLinePoints().elementAt(index);
		p.move(diffx, diffy);
		repaint();
	}

	@Override
	public GridElement cloneFromMe() {
		Relation c = new Relation();

		c.setPanelAttributes(getPanelAttributes());
		c.setAdditionalAttributes(getAdditionalAttributes());

		c.setVisible(true);
		c.setRectangle(getRectangle());
		HandlerElementMap.getHandlerForElement(this).setHandlerAndInitListeners(c);

		return c;
	}

	// Polygon to draw the move whole line rectangle + check if it contains the mouse
	public Polygon getWholeLinePolygon() {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Polygon p = new Polygon();
		Point mid;
		int s = getLinePoints().size();
		if (s % 2 == 0 && s > 0) {
			mid = getCenterOfLine();
		}
		else if (s > 2) {
			Point p1 = getLinePoints().elementAt(getLinePoints().size() / 2);
			Point p2 = getLinePoints().elementAt(getLinePoints().size() / 2 + 1);
			mid = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
		}
		else {
			return null;
		}

		int size = (int) (SELECTBOXSIZE * zoom);
		size = size / 2;
		p.addPoint(mid.x - size, mid.y - size);
		p.addPoint(mid.x + size, mid.y - size);
		p.addPoint(mid.x + size, mid.y + size);
		p.addPoint(mid.x - size, mid.y + size);
		return p;
	}

	// checks if the point is contained in the polygon above
	public boolean isWholeLine(int x, int y) {
		Polygon p = getWholeLinePolygon();
		if (p == null) {
			return false;
		}
		if (p.contains(x, y)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see main.element.base.Entity#paintEntity(java.awt.Graphics) */
	/* (non-Javadoc)
	 * @see main.element.base.Entity#paintEntity(java.awt.Graphics) */
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		// Just to set anti-aliasing, even if no text
		// operations occur

		// g2.setColor(Color.MAGENTA);
		// g2.drawLine(0,0,0,2);
		// g2.drawLine(0,0,2,0);
		// g2.drawLine(this.getWidth()-1-2,this.getHeight()-1, this.getWidth()-1,this.getHeight()-1);
		// g2.drawLine(this.getWidth()-1,this.getHeight()-1-2, this.getWidth()-1,this.getHeight()-1);
		// g2.setColor(activeColor);

		Vector<Rectangle> startShapes = new Vector<Rectangle>();
		Vector<Rectangle> endShapes = new Vector<Rectangle>();

		startShapes.add(new NoShape());
		endShapes.add(new NoShape());

		if (beginQualifier != null && beginQualifier.length() > 0) {
			TextLayout tl = new TextLayout(beginQualifier, HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Qualifier q = new Qualifier(beginQualifier, 0, 0, (int) tl.getBounds().getWidth() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 2, (int) tl.getBounds().getHeight() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2);
			startShapes.add(q);
		}
		if (endQualifier != null && endQualifier.length() > 0) {
			TextLayout tl = new TextLayout(endQualifier, HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont(),
					g2.getFontRenderContext());
			Qualifier q = new Qualifier(endQualifier, 0, 0, (int) tl.getBounds().getWidth() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 2, (int) tl.getBounds().getHeight() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2);
			endShapes.add(q);
		}
		if (beginArrow != null && beginArrow.length() > 0) {
			Arrow a = new Arrow(beginArrow);
			startShapes.add(a);
		}
		if (endArrow != null && endArrow.length() > 0) {
			Arrow a = new Arrow(endArrow);
			endShapes.add(a);
		}
		if (beginMultiplicity != null && beginMultiplicity.length() > 0) {
			EmptyShape e = new EmptyShape((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
			startShapes.add(e);
			TextLayout tl = new TextLayout(beginMultiplicity, HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Multiplicity m = new Multiplicity(beginMultiplicity, 0, 0, (int) tl.getBounds().getWidth(),
					(int) tl.getBounds().getHeight());
			startShapes.add(m);
		}
		if (endMultiplicity != null && endMultiplicity.length() > 0) {
			EmptyShape e = new EmptyShape((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
			endShapes.add(e);
			TextLayout tl = new TextLayout(endMultiplicity, HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Multiplicity m = new Multiplicity(endMultiplicity, 0, 0, (int) tl.getBounds().getWidth(),
					(int) tl.getBounds().getHeight());
			endShapes.add(m);
		}
		if (beginRole != null && beginRole.length() > 0) {
			EmptyShape e = new EmptyShape((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
			startShapes.add(e);
			// A.Mueller start
			// calculating the width if we break lines...

			int position = 0;
			int lineBreaks = 0;
			double broadestText = HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(beginRole);
			while (position != 1) {
				int positionNew = beginRole.indexOf("\\\\", position);
				if (position == 0 && positionNew != -1) {
					broadestText = 0;
				}

				if (positionNew != -1) {

					broadestText = Math.max(broadestText, HandlerElementMap.getHandlerForElement(this).getFontHandler()
							.getTextWidth(beginRole.substring(position,
									positionNew)));
					if (beginRole.lastIndexOf("\\\\") + 2 != beginRole.length()) {
						broadestText = Math.max(broadestText, HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(beginRole.substring(beginRole.lastIndexOf("\\\\") + 2, beginRole.length())));
					}
					lineBreaks++;
				}

				position = positionNew + 2;
			}
			Role r = new Role(beginRole, 0, 0, (int) broadestText, lineBreaks * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (lineBreaks + 2) * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts());

			// <OLDCODE>
			/* TextLayout tl = new TextLayout(beginRole, this.getHandler().getFont(), Constants.getFRC(g2)); Role r = new Role(beginRole, 0, 0, (int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight()); */
			// </OLDCODE>
			// A.Mueller end
			startShapes.add(r);
		}
		if (endRole != null && endRole.length() > 0) {
			EmptyShape e = new EmptyShape((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
			endShapes.add(e);
			// A.Mueller start
			// calculating the width if we break lines...
			int position = 0;
			int lineBreaks = 0;
			double broadestText = HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(endRole);
			while (position != 1) {
				int positionNew = endRole.indexOf("\\\\", position);
				if (position == 0 && positionNew != -1) {
					broadestText = 0;
				}

				if (positionNew != -1) {

					broadestText = Math.max(broadestText, HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(endRole.substring(position, positionNew)));
					if (endRole.lastIndexOf("\\\\") + 2 != endRole.length()) {
						broadestText = Math.max(broadestText,
								HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(endRole.substring(endRole.lastIndexOf("\\\\") + 2,
										endRole.length())));
					}
					lineBreaks++;
				}

				position = positionNew + 2;
			}
			Role r = new Role(endRole, 0, 0, (int) broadestText, lineBreaks * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (lineBreaks + 2) * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts());

			// <OLDCODE>
			/* TextLayout tl = new TextLayout(endRole, this.getHandler().getFont(), Constants.getFRC(g2)); Role r = new Role(endRole, 0, 0, (int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight()); */
			// </OLDCODE>
			// A.Mueller end
			endShapes.add(r);
		}
		// G.Mueller start
		if (beginPort != null && beginPort.length() > 0) {

			EmptyShape e = new EmptyShape((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
			startShapes.add(e);
			TextLayout tl = new TextLayout(beginPort, HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Port p = new Port(beginPort, 0, 0, (int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());

			startShapes.add(p);

		}
		if (endPort != null && endPort.length() > 0) {
			EmptyShape e = new EmptyShape((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
			endShapes.add(e);
			TextLayout tl = new TextLayout(endPort, HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Port p = new Port(endPort, 0, 0, (int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
			endShapes.add(p);
		}
		// G.Mueller end

		// ******************************************************************

		Vector<Point> startPoints = new Vector<Point>(getLinePoints());
		Vector<Point> endPoints = flipVector(startPoints);

		boolean a = lineUp(startShapes, startPoints, 0, 0);
		boolean b = lineUp(endShapes, endPoints, 0, 0);

		if (!a || !b) {
			return;
		}

		// G.Mueller change begin

		if (lineType.equals("-.")) {
			g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
		}
		else if (lineType.equals(".-")) {
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
		}
		else if (lineType.equals(LineType.SOLID.getValue())) {
			g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
		}
		else if (lineType.equals(LineType.DASHED.getValue())) {
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
		}
		else if (lineType.equals(LineType.DOTTED.getValue())) {
			g2.setStroke(Utils.getStroke(LineType.DOTTED, 1));
		}
		else if (lineType.equals(LineType.DOUBLE.getValue())) {
			g2.setStroke(Utils.getStroke(LineType.DOUBLE, 1));
		}
		else if (lineType.equals(LineType.DOUBLE_DASHED.getValue())) {
			g2.setStroke(Utils.getStroke(LineType.DOUBLE_DASHED, 1));
		}
		else if (lineType.equals(LineType.DOUBLE_DOTTED.getValue())) {
			g2.setStroke(Utils.getStroke(LineType.DOUBLE_DOTTED, 1));
		}

		for (int i = 0; i < getLinePoints().size() - 1; i++) {

			if (i == Math.floor((getLinePoints().size() - 1) / 2.0)) {

				Point p1 = getLinePoints().elementAt(i);
				Point p2 = getLinePoints().elementAt(i + 1);
				// G.Mueller start
				Point pm = new Point(p1.x - (p1.x - p2.x) / 2, p1.y - (p1.y - p2.y) / 2);
				g2.drawLine(p1.x, p1.y, pm.x, pm.y);
				if (lineType.equals("-.")) {
					g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				}
				if (lineType.equals(".-")) {
					g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				}
				g2.drawLine(pm.x, pm.y, p2.x, p2.y);
				// g2.drawLine(p1.x, p1.y, p2.x, p2.y);
				// G. Mueller end

				// ##########################################################################################
				// ##########################################################################################
				if (eerRelDir.indexOf("EER_SUBCLASS") >= 0) {
					Point px1 = getLinePoints().elementAt(i);
					Point px2 = getLinePoints().elementAt(i + 1);

					Point mitte = new Point(px1.x - (px1.x - px2.x) / 2, px1.y - (px1.y - px2.y) / 2);

					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = mitte.x;
					int cy = mitte.y;
					double winkel = Utils.getAngle(px1.x, px1.y, px2.x,
							px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);
					g2.setColor(fgColor);
					g2.setStroke(Utils.getStroke(LineType.SOLID, 2));
					g2.drawArc(mitte.x, mitte.y - (int) (10 * zoom), (int) (20 * zoom), (int) (20 * zoom), 90, 180);
					g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
					g2.setTransform(at);

				}
				else if (eerRelDir.indexOf("EER") >= 0) {
					Point px1 = getLinePoints().elementAt(i);
					Point px2 = getLinePoints().elementAt(i + 1);
					Point mitte = new Point(px1.x - (px1.x - px2.x) / 2, px1.y - (px1.y - px2.y) / 2);
					int recSize = (int) (20 * zoom);
					Point r1 = new Point(mitte.x, mitte.y - recSize);
					Point r2 = new Point(mitte.x + recSize, mitte.y);
					Point r3 = new Point(mitte.x, mitte.y + recSize);
					Point r4 = new Point(mitte.x - recSize, mitte.y);

					Polygon po1 = new Polygon();
					po1.addPoint(r1.x, r1.y);
					po1.addPoint(r2.x, r2.y);
					po1.addPoint(r3.x, r3.y);

					Polygon po2 = new Polygon();
					po2.addPoint(r1.x, r1.y);
					po2.addPoint(r3.x, r3.y);
					po2.addPoint(r4.x, r4.y);

					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = mitte.x;
					int cy = mitte.y;
					double winkel = Utils.getAngle(px1.x, px1.y, px2.x, px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);

					if (eerRelDir.equals("EER1")) {
						g2.setColor(fgColor);
						g2.fillPolygon(po1);
						g2.fillPolygon(po2);
					}
					else if (eerRelDir.equals("EER2")) {
						g2.setColor(bgColor);
						g2.fillPolygon(po2);
						g2.setColor(fgColor);
						g2.fillPolygon(po1);
					}
					else if (eerRelDir.equals("EER3")) {
						g2.setColor(bgColor);
						g2.fillPolygon(po1);
						g2.setColor(fgColor);
						g2.fillPolygon(po2);
					}
					g2.setColor(fgColor);
					g2.draw(po1);
					g2.draw(po2);
					g2.setTransform(at);
				}

				// A.Mueller start
				else if (clientServer != null && clientServer.indexOf("rovide") >= 0) {
					Point px1 = getLinePoints().elementAt(i);
					Point px2 = getLinePoints().elementAt(i + 1);
					Point mitte = new Point(px1.x - (px1.x - px2.x) / 2, px1.y - (px1.y - px2.y) / 2);

					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = mitte.x;
					int cy = mitte.y;
					double winkel = Utils.getAngle(px1.x, px1.y, px2.x,
							px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);

					Point outerArc = new Point(mitte.x - (int) (15 * zoom), mitte.y - (int) (15 * zoom));
					Point innerCircle = new Point();

					g2.setColor(Color.white);
					g2.fillOval(outerArc.x, outerArc.y, (int) (30 * zoom), (int) (30 * zoom));
					g2.setColor(fgColor);
					g2.setStroke(Utils.getStroke(LineType.SOLID, 1));

					if (clientServer.equals("provideRequire")) {
						g2.drawArc(outerArc.x, outerArc.y, (int) (30 * zoom), (int) (30 * zoom), 90, 180);
						innerCircle = new Point(mitte.x - (int) (5 * zoom), mitte.y - (int) (10 * zoom));
					}
					else if (clientServer.equals("requireProvide")) {
						g2.drawArc(outerArc.x, outerArc.y, (int) (30 * zoom), (int) (30 * zoom), 90, -180);

						innerCircle = new Point(mitte.x - (int) (15 * zoom), mitte.y - (int) (10 * zoom));
					}

					g2.drawOval(innerCircle.x, innerCircle.y, (int) (20 * zoom), (int) (20 * zoom));
					g2.setTransform(at);
				}
				// A.Mueller end
				// G.Mueller start
				else if (middleArrow.startsWith("delegation")) {

					Point px1 = getLinePoints().elementAt(i);
					Point px2 = getLinePoints().elementAt(i + 1);
					Point mitte = new Point(px1.x - (px1.x - px2.x) / 2, px1.y - (px1.y - px2.y) / 2);

					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = mitte.x;
					int cy = mitte.y;
					double winkel = Utils.getAngle(px1.x, px1.y, px2.x, px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);

					Point circle = new Point(mitte.x - (int) (15 * zoom), mitte.y - (int) (15 * zoom));
					if (middleArrow.equals("delegation")) {
						g2.setColor(Color.white);
						g2.fillOval(circle.x + (int) (5 * zoom), circle.y + (int) (5 * zoom), (int) (20 * zoom), (int) (20 * zoom));
						g2.setColor(fgColor);
						g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
						g2.drawOval(circle.x + (int) (5 * zoom), circle.y + (int) (5 * zoom), (int) (20 * zoom), (int) (20 * zoom));
					}
					if (middleArrow.startsWith("delegationArrow")) {
						g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
						if (middleArrow.equals("delegationArrowRight")) {
							g2.drawLine(circle.x + (int) (5 * zoom), circle.y + (int) (15 * zoom), circle.x - (int) (5 * zoom), circle.y + (int) (9 * zoom));
							g2.drawLine(circle.x + (int) (5 * zoom), circle.y + (int) (15 * zoom), circle.x - (int) (5 * zoom), circle.y + (int) (20 * zoom));
						}
						if (middleArrow.equals("delegationArrowLeft")) {
							g2.drawLine(circle.x + (int) (25 * zoom), circle.y + (int) (15 * zoom), circle.x + (int) (35 * zoom), circle.y + (int) (9 * zoom));
							g2.drawLine(circle.x + (int) (25 * zoom), circle.y + (int) (15 * zoom), circle.x + (int) (35 * zoom), circle.y + (int) (20 * zoom));
						}

						g2.setColor(Color.white);
						g2.fillOval(circle.x + (int) (5 * zoom), circle.y + (int) (5 * zoom), (int) (20 * zoom), (int) (20 * zoom));
						g2.setColor(fgColor);
						g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
						g2.drawOval(circle.x + (int) (5 * zoom), circle.y + (int) (5 * zoom), (int) (20 * zoom), (int) (20 * zoom));

					}
					if (middleArrow.equals("delegationStart")) {
						g2.setColor(Color.white);
						g2.fillArc(circle.x, circle.y, (int) (30 * zoom), (int) (30 * zoom), 90, 180);
						g2.setColor(fgColor);
						g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
						g2.drawArc(circle.x, circle.y, (int) (30 * zoom), (int) (30 * zoom), 90, 180);
					}
					if (middleArrow.equals("delegationEnd")) {
						g2.setColor(Color.white);
						g2.fillArc(circle.x, circle.y, (int) (30 * zoom), (int) (30 * zoom), 90, -180);
						g2.setColor(fgColor);
						g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
						g2.drawArc(circle.x, circle.y, (int) (30 * zoom), (int) (30 * zoom), 90, -180);
					}

					g2.setTransform(at);

				}

				// G.Mueller end

				// ##########################################################################################
				// ##########################################################################################
				if (lineType.equals("-.")) {
					g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				}
				if (lineType.equals(".-")) {
					g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				}

			}
			else {

				Point p1 = getLinePoints().elementAt(i);
				Point p2 = getLinePoints().elementAt(i + 1);
				g2.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}

		g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			for (int i = 0; i < getLinePoints().size(); i++) {
				Point p = getLinePoints().elementAt(i);
				int start = (int) (SELECTCIRCLESIZE / 15 * 10 * zoom);
				int width = (int) (SELECTCIRCLESIZE / 15 * 20 * zoom);
				g2.drawOval(p.x - start, p.y - start, width, width);
			}

			// DRAW Moveall Rect
			Polygon poly = getWholeLinePolygon();
			if (poly != null) {
				g2.drawPolygon(poly);
			}
		}

		Vector<Rectangle> tmp = new Vector<Rectangle>(startShapes);
		tmp.addAll(endShapes);
		for (int i = 0; i < tmp.size(); i++) {
			Rectangle r = tmp.elementAt(i);
			if (r instanceof Qualifier) {

				Qualifier q = (Qualifier) r;

				// begin B. Buckl
				g.setColor(bgColor);
				g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
				g.setColor(fgColor);
				// end
				g.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, q.getString(), r.getX() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), r.getY() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), AlignHorizontal.LEFT);

			}
			else if (r instanceof Arrow) {
				Arrow arrow = (Arrow) r;

				if (arrow.getString().equals("crowsFoot")) {

					g2.drawLine(arrow.getX(), arrow.getY() + arrow.getArrowEndA().y,
							arrow.getX() + arrow.getArrowEndA().x, arrow.getY());
					g2.drawLine(arrow.getX(), arrow.getY() + arrow.getArrowEndB().y,
							arrow.getX() + arrow.getArrowEndB().x, arrow.getY());

					// A.Mueller Start
				}
				else if (!arrow.getString().equals("n") && !arrow.getString().equals("require") && !arrow.getString().equals("provide") && !arrow.getString().startsWith("package") && !arrow.getString().startsWith("comp")) {
					// A.Mueller end
					g2.drawLine(arrow.getX(), arrow.getY(),
							arrow.getX() + arrow.getArrowEndA().x,
							arrow.getY() + arrow.getArrowEndA().y);
					g2.drawLine(arrow.getX(), arrow.getY(),
							arrow.getX() + arrow.getArrowEndB().x,
							arrow.getY() + arrow.getArrowEndB().y);
					// A.Mueller start
				}
				// A.Mueller end

				if (arrow.getString().equals("<<<")) { // LME
					// filled arrow head
					int[] ax = new int[3];
					int[] ay = new int[3];
					ax[0] = arrow.getX();
					ax[1] = arrow.getX() + arrow.getArrowEndA().x;
					ax[2] = arrow.getX() + arrow.getArrowEndB().x;
					ay[0] = arrow.getY();
					ay[1] = arrow.getY() + arrow.getArrowEndA().y;
					ay[2] = arrow.getY() + arrow.getArrowEndB().y;
					Polygon myPg = new Polygon(ax, ay, 3);
					g2.fill(myPg);
					g2.draw(myPg);
				}
				else if (arrow.getString().equals("<<")) {
					// begin B. Buckl
					int[] ax = new int[3];
					int[] ay = new int[3];
					ax[0] = arrow.getX();
					ax[1] = arrow.getX() + arrow.getArrowEndA().x;
					ax[2] = arrow.getX() + arrow.getArrowEndB().x;
					ay[0] = arrow.getY();
					ay[1] = arrow.getY() + arrow.getArrowEndA().y;
					ay[2] = arrow.getY() + arrow.getArrowEndB().y;
					Polygon myPg = new Polygon(ax, ay, 3);
					g2.setColor(bgColor);
					g2.fill(myPg);
					g2.setColor(fgColor);
					g2.draw(myPg);

					// g2.drawLine((int)arrow.getX()+(int)arrow.getArrowEndA().x,
					// (int)arrow.getY()+(int)arrow.getArrowEndA().y,
					// (int)arrow.getX()+(int)arrow.getArrowEndB().x,
					// (int)arrow.getY()+(int)arrow.getArrowEndB().y);
				} // end B. Buckl
				else if (arrow.getString().equals("x")) {
					int[] ax = new int[4];
					int[] ay = new int[4];
					ax[0] = arrow.getX();
					ay[0] = arrow.getY();
					ax[1] = arrow.getX() + arrow.getArrowEndA().x;
					ay[1] = arrow.getY() + arrow.getArrowEndA().y;
					ax[3] = arrow.getX() + arrow.getArrowEndB().x;
					ay[3] = arrow.getY() + arrow.getArrowEndB().y;

					ax[2] = -arrow.getX() + ax[1] + ax[3];
					ay[2] = -arrow.getY() + ay[1] + ay[3];

					// begin B. Buckl
					Polygon myPg = new Polygon(ax, ay, 4);
					g2.setColor(bgColor);
					g2.fill(myPg);
					g2.setColor(fgColor);
					g2.draw(myPg);
					// end B. Buckl
				}
				else if (arrow.getString().equals("X")) {
					int[] ax = new int[4];
					int[] ay = new int[4];
					ax[0] = arrow.getX();
					ay[0] = arrow.getY();
					ax[1] = arrow.getX() + arrow.getArrowEndA().x;
					ay[1] = arrow.getY() + arrow.getArrowEndA().y;
					ax[3] = arrow.getX() + arrow.getArrowEndB().x;
					ay[3] = arrow.getY() + arrow.getArrowEndB().y;

					ax[2] = -arrow.getX() + ax[1] + ax[3];
					ay[2] = -arrow.getY() + ay[1] + ay[3];

					g2.fill(new Polygon(ax, ay, 4));
				}
				// A.Mueller Begin
				else if (arrow.getString().equals("n")) {
					Point a1 = arrow.getCrossEndA();
					Point a2 = arrow.getCrossEndB();
					g2.drawLine(arrow.getX() + arrow.getArrowEndA().x,
							arrow.getY() + arrow.getArrowEndA().y,
							arrow.getX() + a2.x,
							arrow.getY() + a2.y);
					g2.drawLine(arrow.getX() + arrow.getArrowEndB().x,
							arrow.getY() + arrow.getArrowEndB().y,
							arrow.getX() + a1.x,
							arrow.getY() + a1.y);

				}
				else if (arrow.getString().equals("require")) {

					int width = arrow.getCrossEndB().x - arrow.getCrossEndA().x;
					int height = arrow.getCrossEndB().y - arrow.getCrossEndA().y;
					g2.drawOval(arrow.getX() + arrow.getCrossEndA().x, arrow.getY() + arrow.getCrossEndA().y, width, height);

				}
				else if (arrow.getString().equals("provide")) {
					int width = arrow.getCrossEndB().x - arrow.getCrossEndA().x;
					int height = arrow.getCrossEndB().y - arrow.getCrossEndA().y;
					g2.drawArc(arrow.getX() + arrow.getCrossEndA().x, arrow.getY() + arrow.getCrossEndA().y, width, height, arrow.getArcStart(), arrow.getArcEnd());
					// A.Mueller End
					// G.Mueller Start
				}
				else if (arrow.getString().startsWith("package")) {
					Point px1;
					Point px2;
					if (arrow.getString().equals("packageStart")) {
						px1 = getStartPoint();
						px2 = getLinePoints().elementAt(1);
					}
					else {
						px1 = getEndPoint();
						px2 = getLinePoints().elementAt(
								getLinePoints().size() - 2);
					}
					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = px1.x;
					int cy = px1.y;
					double winkel = Utils.getAngle(px1.x, px1.y, px2.x, px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);
					g2.setColor(bgColor);
					g2.fillOval(px1.x, px1.y - (int) (10 * zoom), (int) (20 * zoom), (int) (20 * zoom));
					g2.setColor(fgColor);
					g2.drawOval(px1.x, px1.y - (int) (10 * zoom), (int) (20 * zoom), (int) (20 * zoom));
					g2.drawLine(px1.x + (int) (10 * zoom), px1.y - (int) (5 * zoom), px1.x + (int) (10 * zoom), px1.y + (int) (5 * zoom));
					g2.drawLine(px1.x + (int) (15 * zoom), px1.y, px1.x + (int) (5 * zoom), px1.y);
					g2.setTransform(at);

					// ***
					// Wirrer G. Start
				}
				else if (arrow.getString().startsWith("fill_poly")) {

					Point px1;
					Point px2;
					if (beginArrow.startsWith("fill_poly_start")) {
						px1 = getStartPoint();
						px2 = getLinePoints().elementAt(1);
						AffineTransform at = g2.getTransform();
						AffineTransform at2 = (AffineTransform) at.clone();
						double winkel = Utils.getAngle(px1.x, px1.y, px2.x, px2.y);
						at2.rotate(winkel, px1.x, px1.y);
						g2.setTransform(at2);
						int[] x_cord = { px1.x, px1.x + (int) (13 * zoom), px1.x + (int) (13 * zoom) };
						int[] y_cord = { px1.y, px1.y - (int) (7 * zoom), px1.y + (int) (7 * zoom) };
						Polygon x = new Polygon(x_cord, y_cord, 3);
						g2.fillPolygon(x);
						g2.setTransform(at);
					}
					if (endArrow.startsWith("fill_poly_end")) {
						px1 = getEndPoint();
						px2 = getLinePoints().elementAt(getLinePoints().size() - 2);
						AffineTransform at = g2.getTransform();
						AffineTransform at2 = (AffineTransform) at.clone();
						double winkel = Utils.getAngle(px2.x, px2.y, px1.x, px1.y);
						at2.rotate(winkel, px1.x, px1.y);
						g2.setTransform(at2);
						int[] x_cord = { px1.x, px1.x - (int) (13 * zoom), px1.x - (int) (13 * zoom) };
						int[] y_cord = { px1.y, px1.y - (int) (7 * zoom), px1.y + (int) (7 * zoom) };
						Polygon x = new Polygon(x_cord, y_cord, 3);
						g2.fillPolygon(x);
						g2.setTransform(at);
					}
					// Wirrer G. End
				}
				else if (arrow.getString().startsWith("comp")) {

					Point px1;
					Point px2;
					int s;

					int boxSize = (int) (20 * zoom);
					// we use 5.9 and 6.9 instead of 6 and 7 to get bigger arrows if we zoom out but smaller if gridsize is 10
					int arrowOneSize = (int) (5.9 * zoom);
					int arrowTwoSize = (int) (6.9 * zoom);
					int arrowTwoSeparator = (int) (2.4 * zoom);
					int arrowThreeSize = (int) (6 * zoom);
					int arrowThreeLength = (int) (12 * zoom);

					// if (beginCSDArrow.equals("compStart")) {
					if (beginArrow.startsWith("compStart")) {
						HandlerElementMap.getHandlerForElement(this).getFontHandler().setFontSize((double) 10);

						s = boxSize;

						if (!csdStartText.equals("")) {
							s = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(csdStartText);
						}
						if (s < boxSize) {
							s = boxSize;
						}

						px1 = getStartPoint();
						px2 = getLinePoints().elementAt(1);
						g2.setColor(bgColor);
						g2.fillRect(px1.x - s / 2, px1.y - s / 2, s, s);
						g2.setColor(fgColor);
						g2.drawRect(px1.x - s / 2, px1.y - s / 2, s, s);
						if (csdStartText.equals(">")) {
							int[] tmpX = { px1.x - arrowOneSize, px1.x + arrowOneSize, px1.x - arrowOneSize };
							int[] tmpY = { px1.y - arrowOneSize, px1.y, px1.y + arrowOneSize };
							g2.fillPolygon(tmpX, tmpY, 3);
						}
						else if (csdStartText.equals("<")) {
							int[] tmpX = { px1.x + arrowOneSize, px1.x - arrowOneSize, px1.x + arrowOneSize };
							int[] tmpY = { px1.y - arrowOneSize, px1.y, px1.y + arrowOneSize };
							g2.fillPolygon(tmpX, tmpY, 3);
						}
						else if (csdStartText.equals("v")) {
							int[] tmpX = { px1.x - arrowOneSize, px1.x, px1.x + arrowOneSize };
							int[] tmpY = { px1.y - arrowOneSize, px1.y + arrowOneSize, px1.y - arrowOneSize };
							g2.fillPolygon(tmpX, tmpY, 3);
						}
						else if (csdStartText.equals("^")) {
							int[] tmpX = { px1.x - arrowOneSize, px1.x, px1.x + arrowOneSize };
							int[] tmpY = { px1.y + arrowOneSize, px1.y - arrowOneSize, px1.y + arrowOneSize };
							g2.fillPolygon(tmpX, tmpY, 3);
						}
						else if (csdStartText.equals("=")) {
							g2.drawLine(px1.x - arrowTwoSize, px1.y - arrowTwoSeparator, px1.x + arrowTwoSize, px1.y - arrowTwoSeparator);
							g2.drawLine(px1.x + arrowTwoSize, px1.y - arrowTwoSeparator, px1.x + 1, px1.y - arrowTwoSize);
							g2.drawLine(px1.x - arrowTwoSize, px1.y + arrowTwoSeparator, px1.x + arrowTwoSize, px1.y + arrowTwoSeparator);
							g2.drawLine(px1.x - arrowTwoSize, px1.y + arrowTwoSeparator, px1.x - 1, px1.y + arrowTwoSize);
						}
						else {
							if (!csdStartText.equals("")) {
								HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, csdStartText, px1.x, px1.y + (int) (6 * zoom), AlignHorizontal.CENTER);
							}
						}

						if (beginArrow.equals("compStartdel")) {
							AffineTransform at = g2.getTransform();
							AffineTransform at2 = (AffineTransform) at.clone();
							int cx = px1.x;
							int cy = px1.y;
							double winkel = Utils.getAngle(px1.x, px1.y, px2.x,
									px2.y);
							at2.rotate(winkel, cx, cy);
							g2.setTransform(at2);
							g2.drawLine((int) (px1.x + s / 2 + 2 * zoom), px1.y, px1.x + s / 2 + arrowThreeLength, px1.y - arrowThreeSize);
							g2.drawLine((int) (px1.x + s / 2 + 2 * zoom), px1.y, px1.x + s / 2 + arrowThreeLength, px1.y + arrowThreeSize);
							g2.setTransform(at);
						}

						HandlerElementMap.getHandlerForElement(this).getFontHandler().resetFontSize();
					}

					// if (endCSDArrow.equals("compEnd")) {
					if (endArrow.startsWith("compEnd")) {
						HandlerElementMap.getHandlerForElement(this).getFontHandler().setFontSize(10.0);

						s = boxSize;

						if (!csdEndText.equals("")) {
							s = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(csdEndText);
						}
						if (s < boxSize) {
							s = boxSize;
						}

						px1 = getEndPoint();
						px2 = getLinePoints().elementAt(
								getLinePoints().size() - 2);
						g2.setColor(bgColor);
						g2.fillRect(px1.x - s / 2, px1.y - s / 2, s, s);
						g2.setColor(fgColor);
						g2.drawRect(px1.x - s / 2, px1.y - s / 2, s, s);
						if (csdEndText.equals(">")) {
							int[] tmpX = { px1.x - arrowOneSize, px1.x + arrowOneSize, px1.x - arrowOneSize };
							int[] tmpY = { px1.y - arrowOneSize, px1.y, px1.y + arrowOneSize };
							g2.fillPolygon(tmpX, tmpY, 3);
						}
						else if (csdEndText.equals("<")) {
							int[] tmpX = { px1.x + arrowOneSize, px1.x - arrowOneSize, px1.x + arrowOneSize };
							int[] tmpY = { px1.y - arrowOneSize, px1.y, px1.y + arrowOneSize };
							g2.fillPolygon(tmpX, tmpY, 3);
						}
						else if (csdEndText.equals("v")) {
							int[] tmpX = { px1.x - arrowOneSize, px1.x, px1.x + arrowOneSize };
							int[] tmpY = { px1.y - arrowOneSize, px1.y + arrowOneSize, px1.y - arrowOneSize };
							g2.fillPolygon(tmpX, tmpY, 3);
						}
						else if (csdEndText.equals("^")) {
							int[] tmpX = { px1.x - arrowOneSize, px1.x, px1.x + arrowOneSize };
							int[] tmpY = { px1.y + arrowOneSize, px1.y - arrowOneSize, px1.y + arrowOneSize };
							g2.fillPolygon(tmpX, tmpY, 3);
						}
						else if (csdEndText.equals("=")) {
							g2.drawLine(px1.x - arrowTwoSize, px1.y - arrowTwoSeparator, px1.x + arrowTwoSize, px1.y - arrowTwoSeparator);
							g2.drawLine(px1.x + arrowTwoSize, px1.y - arrowTwoSeparator, px1.x + 1, px1.y - arrowTwoSize);
							g2.drawLine(px1.x - arrowTwoSize, px1.y + arrowTwoSeparator, px1.x + arrowTwoSize, px1.y + arrowTwoSeparator);
							g2.drawLine(px1.x - arrowTwoSize, px1.y + arrowTwoSeparator, px1.x - 1, px1.y + arrowTwoSize);
						}
						else {
							if (!csdEndText.equals("")) {
								HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, csdEndText, px1.x, px1.y + (int) (6 * zoom), AlignHorizontal.CENTER);
							}
						}

						if (endArrow.equals("compEnddel")) {
							AffineTransform at = g2.getTransform();
							AffineTransform at2 = (AffineTransform) at.clone();
							int cx = px1.x;
							int cy = px1.y;
							double winkel = Utils.getAngle(px1.x, px1.y, px2.x,
									px2.y);
							at2.rotate(winkel, cx, cy);
							g2.setTransform(at2);
							g2.drawLine((int) (px1.x + s / 2 + 2 * zoom), px1.y, px1.x + s / 2 + arrowThreeLength, px1.y - arrowThreeSize);
							g2.drawLine((int) (px1.x + s / 2 + 2 * zoom), px1.y, px1.x + s / 2 + arrowThreeLength, px1.y + arrowThreeSize);
							g2.setTransform(at);
						}

						HandlerElementMap.getHandlerForElement(this).getFontHandler().resetFontSize();
					}

				}
				// G.Mueller End

			}
			else if (r instanceof Multiplicity) {
				Multiplicity m = (Multiplicity) r;
				// g.drawRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(),
				// (int)r.getHeight());
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, m.getString(), r.getX(), r.getY() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + 2 * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), AlignHorizontal.LEFT); // B. Buckl
				// added
				// +2*this.getHandler().getDistTextToText()
			}
			else if (r instanceof Role) {
				Role role = (Role) r;
				String str = role.getString();

				int position = 0;
				int y = 4 * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				while (position != -1) {
					position = str.indexOf("\\\\");

					if (position != -1) {
						String s = str.substring(0, position);
						HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, r.getX(), r.getY() + y, AlignHorizontal.LEFT);

						y = y + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
						str = str.substring(position + 2, str.length());
					}
					else {
						HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, str, r.getX(), r.getY() + y, AlignHorizontal.LEFT);

					}

				}

				// <OLDCODE>
				/* this.getHandler().write(g2, role.getString(), (int) r.getX(), (int) r .getY() + this.getHandler().getFontHandler().getFontsize() + 2 this.getHandler().getDistTextToText(), false); // B. Buckl // added // +2*this.getHandler().getDistTextToText() */
				// </OLDCODE>
				// A.Mueller end
				// G.Mueller Start
			}
			else if (r instanceof Port) {
				Port p = (Port) r;

				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, p.getString(), r.getX(), r.getY(), AlignHorizontal.LEFT);

			}
			// G.Mueller end
		}

		if (getStrings() != null) {
			if (getStrings().size() > 0) {
				Point start = getCenterOfLine();
				int yPos = start.y - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(); // B. Buckl
				// added
				// -this.getHandler().getDistTextToText()
				int xPos = start.x;
				for (int i = 0; i < getStrings().size(); i++) {
					String s = getStrings().elementAt(i);

					// A.Mueller Begin...
					if (s.startsWith(">") || s.endsWith(">") || s.startsWith("<") || s.endsWith("<")) {
						// starts or ends with an arrow, check if it is the only
						// one..
						if (s.indexOf(">") == s.lastIndexOf(">") && s.indexOf(">") != -1 || s.indexOf("<") == s.lastIndexOf("<") && s.indexOf("<") != -1) {
							// decide where and what to draw...
							int fontHeight = g2.getFontMetrics(
									HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont()).getHeight() - g2.getFontMetrics(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont()).getDescent() - g2.getFontMetrics(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont()).getLeading();
							fontHeight = fontHeight / 3 * 2;

							if (s.endsWith(">")) {
								s = s.substring(0, s.length() - 1);
								int fontWidth = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(s);
								xPos = xPos - (fontHeight + 4) / 2;
								int startDrawX = xPos + fontWidth / 2 + 4;
								Polygon temp = new Polygon();
								temp.addPoint(startDrawX, yPos);
								temp.addPoint(startDrawX, yPos - fontHeight);
								temp.addPoint(startDrawX + fontHeight - 1, yPos - fontHeight / 2);
								g2.fillPolygon(temp);

							}
							else if (s.endsWith("<")) {
								s = s.substring(0, s.length() - 1);
								int fontWidth = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(s);
								xPos = xPos - (fontHeight + 4) / 2;
								int startDrawX = xPos + fontWidth / 2 + 4;
								Polygon temp = new Polygon();
								temp.addPoint(startDrawX + fontHeight - 1, yPos);
								temp.addPoint(startDrawX + fontHeight - 1, yPos - fontHeight);
								temp.addPoint(startDrawX, yPos - fontHeight / 2);
								g2.fillPolygon(temp);
							}
							else if (s.startsWith(">")) {
								s = s.substring(1, s.length());
								int fontWidth = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(s);
								xPos = xPos + (fontHeight + 4) / 2;
								int startDrawX = xPos - fontWidth / 2 - 4;
								Polygon temp = new Polygon();
								temp.addPoint(startDrawX - fontHeight + 1, yPos);
								temp.addPoint(startDrawX - fontHeight + 1, yPos - fontHeight);
								temp.addPoint(startDrawX, yPos - fontHeight / 2);
								g2.fillPolygon(temp);
							}
							else if (s.startsWith("<")) {
								s = s.substring(1, s.length());
								int fontWidth = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(s);
								xPos = xPos + (fontHeight + 4) / 2;
								int startDrawX = xPos - fontWidth / 2 - 4;
								Polygon temp = new Polygon();
								temp.addPoint(startDrawX, yPos);
								temp.addPoint(startDrawX, yPos - fontHeight);
								temp.addPoint(startDrawX - fontHeight + 1, yPos - fontHeight / 2);
								g2.fillPolygon(temp);

							}

						}

					}
					// A.Mueller end...

					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, xPos, yPos, AlignHorizontal.CENTER);
					yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
					yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				}
			}
		}

		// The criticalPoints must be calculated to expand the relations size by the associated relation-text
		Vector<Point> criticalPoints = new Vector<Point>();
		for (int i = 1; i < startShapes.size(); i++) {
			Rectangle r = startShapes.elementAt(i);
			Point p1 = new Point(r.getX() - (int) (2 * zoom), r.getY() - (int) (2 * zoom));
			Point p2 = new Point(r.getX() + r.getWidth() + (int) (2 * zoom),
					r.getY() + r.getHeight() + (int) (2 * zoom));
			criticalPoints.add(p1);
			criticalPoints.add(p2);
		}
		for (int i = 1; i < endShapes.size(); i++) {
			Rectangle r = endShapes.elementAt(i);
			Point p1 = new Point(r.getX() - (int) (2 * zoom), r.getY() - (int) (2 * zoom));
			Point p2 = new Point(r.getX() + r.getWidth() + (int) (2 * zoom),
					r.getY() + r.getHeight() + (int) (2 * zoom));
			criticalPoints.add(p1);
			criticalPoints.add(p2);
		}
		if (getStrings() != null) {
			if (getStrings().size() > 0) {
				Point start = getCenterOfLine();
				int yPos = start.y;
				int xPos = start.x;
				for (int i = 0; i < getStrings().size(); i++) {
					String s = getStrings().elementAt(i);
					int width = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(s);
					criticalPoints.add(new Point(xPos - width / 2 - (int) (20 * zoom), yPos - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() - (int) (20 * zoom)));
					criticalPoints.add(new Point(xPos + width / 2 + (int) (20 * zoom), yPos + (int) (20 * zoom)));
					yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
					yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				}
			}
		}

		/**
		 * Change Size of relation to fit the relations size
		 * Must be made to resize the relation automatically during draging an endpoint
		 */

		// (minx,miny) is the upper left end and (maxx,maxy) is the lower right end of the relation
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE;
		int maxy = Integer.MIN_VALUE;
		for (int i = 0; i < getLinePoints().size(); i++) {
			Point p = getLinePoints().elementAt(i);
			minx = Math.min(minx, p.x);
			miny = Math.min(miny, p.y);
			maxx = Math.max(maxx, p.x);
			maxy = Math.max(maxy, p.y);

			// Subtract or add the SELECTCIRCLESIZE to avoid cutting the circles at the end of the relation
			minx = (int) Math.min(minx, p.x - SELECTCIRCLESIZE * zoom);
			miny = (int) Math.min(miny, p.y - SELECTCIRCLESIZE * zoom);
			maxx = (int) Math.max(maxx, p.x + SELECTCIRCLESIZE * zoom);
			maxy = (int) Math.max(maxy, p.y + SELECTCIRCLESIZE * zoom);
		}
		for (int i = 0; i < criticalPoints.size(); i++) {
			Point p = criticalPoints.elementAt(i);
			minx = Math.min(minx, p.x);
			miny = Math.min(miny, p.y);
			maxx = Math.max(maxx, p.x);
			maxy = Math.max(maxy, p.y);
		}

		// BUGFIX ZOOM: We must consider the gridsize for the min and max value to avoid rounding errors
		// Therefore we subtract or add the difference to the next possible value
		int gridSize = HandlerElementMap.getHandlerForElement(this).getGridSize();

		minx -= minx % gridSize;
		miny -= miny % gridSize;

		// Subtract gridSize another time to avoid a too small selection area for the relation-selection circle
		minx -= gridSize;
		miny -= gridSize;

		maxx += maxx % gridSize;
		maxy += maxy % gridSize;

		if (maxx != 0 || maxy != 0) {
			int diffx = maxx - getRectangle().width;
			int diffy = maxy - getRectangle().height;
			this.setSize(getRectangle().width + diffx, getRectangle().height + diffy);
		}
		if (minx != 0 | miny != 0) {
			setLocationDifference(minx, miny);
			this.setSize(getRectangle().width + -minx, getRectangle().height + -miny);
			for (int i = 0; i < getLinePoints().size(); i++) {
				Point p = getLinePoints().elementAt(i);
				p.x += -minx;
				p.y += -miny;
			}
		}

	}

	private Point getStartPoint() {
		Point ret = getLinePoints().elementAt(0);
		return ret;
	}

	private Point getEndPoint() {
		Point ret = getLinePoints().elementAt(
				getLinePoints().size() - 1);
		return ret;
	}

	public PointDouble getAbsoluteCoorStart() {
		PointDouble ret = new PointDouble(getRectangle().x + getStartPoint().x, getRectangle().y + getStartPoint().y);
		return ret;
	}

	public PointDouble getAbsoluteCoorEnd() {
		PointDouble ret = new PointDouble(getRectangle().x + getEndPoint().x, getRectangle().y + getEndPoint().y);
		return ret;
	}

	// G.Mueller start
	public String[] getCSDText(String str) { // for the Composite Structure Diagram Text

		String[] tmp = new String[4];
		int to = 0;
		int from = 0;
		tmp[0] = " ";
		tmp[1] = " ";
		tmp[2] = " ";
		tmp[3] = " ";

		if (str.length() > 3) {

			// if (str.indexOf("<[") >=3) tmp[2] =
			// str.substring(3,str.indexOf("<["));
			// if (str.lastIndexOf("[") >=3 && str.lastIndexOf("[")-1 !=
			// str.lastIndexOf("<[")) tmp[3] = str.substring(str.indexOf("[",
			// str.length()));

			from = str.indexOf("<[") + 2;
			if (from >= 2) {
				to = str.indexOf("]");
			}
			if (from >= 2 && to >= 0 && from < to) {
				tmp[0] = str.substring(from, to);
			}

			from = str.indexOf("[", to) + 1;
			if (from >= 1) {
				to = str.indexOf("]>", to);
			}
			if (from >= 1 && to >= 0 && from < to) {
				tmp[1] = str.substring(from, to);
			}

		}

		return tmp;

	}

	// G.Mueller end

	@Override
	public StickingPolygon generateStickingBorder() { // LME
		return NoStickingPolygonGenerator.INSTANCE.generateStickingBorder(getRectangle());
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		return NoStickingPolygonGenerator.INSTANCE.generateStickingBorder(getRectangle());
	}

	public boolean allPointsOnSamePos() {
		Point first = null;
		for (Point p : getLinePoints()) {
			if (first == null) {
				first = p;
			}
			else {
				if (first.x != p.x || first.y != p.y) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected Color getDefaultBackgroundColor() {
		return Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.WHITE));
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return Collections.<Direction> emptySet();
	}

	@Override
	public Integer getLayer() {
		return getLayerHelper(LayerFacet.DEFAULT_VALUE_RELATION);
	}

}
