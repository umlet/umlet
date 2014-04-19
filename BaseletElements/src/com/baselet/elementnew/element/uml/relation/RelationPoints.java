package com.baselet.elementnew.element.uml.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.GeometricFunctions;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;

public class RelationPoints {

	public static final int DRAG_BOX_SIZE = 10; // size of the box to drag the whole relation
	public static final int POINT_SELECTION_RADIUS = 10; // radius of the selection circle of relation-points
	public static final int NEW_POINT_DISTANCE = 7; // distance from which new points can be dragged away from a relation-line

	/**
	 * Points of this relation (point of origin is the upper left corner of the relation element (not the drawpanel!))
	 */
	private List<PointDouble> points = new ArrayList<PointDouble>();
	private Relation relation;

	public RelationPoints(Relation relation, List<PointDouble> points) {
		super();
		this.relation = relation;
		this.points = points;
	}

	static enum Selection {
		RELATION_POINT,
		DRAG_BOX,
		LINE,
		NOTHING;
	}

	public Selection getSelection(Point point) {
		if (isPointOverDragBox(point)) {
			return Selection.DRAG_BOX;
		} else if (RelationPointsUtils.getRelationPointContaining(point, points) != null) {
			return Selection.RELATION_POINT;
		} else if (getLineContaining(point) != null) {
			return Selection.LINE;
		} else {
			return Selection.NOTHING;
		}
	}

	private PointDouble relationPointOfCurrentDrag = null;
	/**
	 * this method is basically the same as {@link #getSelection(Point)}, but also applies changes to the relationpoints
	 * (the order of checks is the same, but they do different things, therefore they are separated)
	 */
	public Selection getSelectionAndMovePointsIfNecessary(Point point, Integer diffX, Integer diffY, boolean firstDrag) {
		// Special case: if this is not the first drag and a relation-point is currently dragged, it has preference
		// Necessary to avoid changing the currently moved point if moving over another point and to avoid losing the current point if it's a new line point and the mouse is dragged very fast
		if (!firstDrag && relationPointOfCurrentDrag != null) {
			relationPointOfCurrentDrag = movePointAndResizeRectangle(relationPointOfCurrentDrag, diffX, diffY);
			return Selection.RELATION_POINT;
		}
		// If the special case doesn't apply, forget the relationPointOfFirstDrag, because its a new first drag
		relationPointOfCurrentDrag = null;
		if (isPointOverDragBox(point)) {
			return Selection.DRAG_BOX;
		}
		PointDouble pointOverRelationPoint = RelationPointsUtils.getRelationPointContaining(point, points);
		if (pointOverRelationPoint != null) {
			relationPointOfCurrentDrag = movePointAndResizeRectangle(pointOverRelationPoint, diffX, diffY);
			return Selection.RELATION_POINT;
		}
		Line lineOnPoint = getLineContaining(point);
		if (lineOnPoint != null) {
			PointDouble roundedPoint = new PointDouble(SharedUtils.realignToGridRoundToNearest(false, point.x), SharedUtils.realignToGridRoundToNearest(false, point.y));
			points.add(points.indexOf(lineOnPoint.getEnd()), roundedPoint);
			relationPointOfCurrentDrag = movePointAndResizeRectangle(roundedPoint, diffX, diffY);
			return Selection.LINE;
		}
		return Selection.NOTHING;
	}

	private boolean isPointOverDragBox(Point point) {
		return getDragBox().contains(point);
	}

	private Line getLineContaining(Point point) {
		for (Line line : getRelationPointLines()) {
			double distanceToPoint = line.getDistanceToPoint(point.toPointDouble());
			if (distanceToPoint < NEW_POINT_DISTANCE) {
				return line;
			}
		}
		return null;
	}

	PointDouble movePointAndResizeRectangle(PointDouble point, Integer diffX, Integer diffY) {
		// move the point
		PointDouble movedPoint = RelationPointsUtils.movePointOnPosition(points, point, diffX, diffY);
		// if there are only 2 points and they would overlap now (therefore the relation would have a size of 0x0px), revert the move
		if (points.size() == 2 && points.get(0).equals(points.get(1))) {
			movedPoint = RelationPointsUtils.movePointOnPosition(points, point, -diffX, -diffY);
		}
		int idx = points.indexOf(movedPoint);
		resizeRectAndReposPoints();
		return points.get(idx);
	}

	void resizeRectAndReposPoints() {
		// now rebuild width and height of the relation, based on the new positions of the relation-points
		Rectangle newRect = RelationPointsUtils.calculateRelationRectangleBasedOnPoints(relation.getRectangle().getUpperLeftCorner(), relation.getGridSize(), points);
		relation.setRectangle(newRect);

		// move relation points to their new position (their position is relative to the relation-position)
		RelationPointsUtils.moveRelationPointsOriginToUpperLeftCorner(points);
	}

	public boolean removeRelationPointIfOnLineBetweenNeighbourPoints() {
		boolean updateNecessary = false;
		if (points.size() > 2) {
			ListIterator<PointDouble> iter = points.listIterator();
			PointDouble leftNeighbour = iter.next();
			PointDouble pointToCheck = iter.next();
			while (iter.hasNext()) {
				PointDouble rightNeighbour = iter.next();
				// if a point lies on the line between its 2 neighbourpoints, it will be removed
				if (GeometricFunctions.getDistanceBetweenLineAndPoint(leftNeighbour, rightNeighbour, pointToCheck) < 5) {
					updateNecessary = true;
					iter.previous();
					iter.previous();
					iter.remove();
					pointToCheck = iter.next();
				} else {
					leftNeighbour = pointToCheck;
					pointToCheck = rightNeighbour;
				}
			}
		}
		return updateNecessary;
	}

	// HELPER METHODS

	public List<Line> getRelationPointLines() {
		List<Line> lines = new ArrayList<Line>();
		for (int i = 1; i < points.size(); i++) {
			lines.add(new Line(points.get(i - 1), points.get(i)));
		}
		return lines;
	}

	public Line getFirstLine() {
		return new Line(points.get(0), points.get(1));
	}

	public Line getLastLine() {
		return new Line(points.get(points.size()-2), points.get(points.size()-1));
	}

	public Collection<PointDouble> getStickablePoints() {
		return Arrays.asList(points.get(0), points.get(points.size()-1));
	}

	public Rectangle getDragBox() {
		PointDouble begin = points.get(points.size() / 2);
		PointDouble end = points.get(points.size() / 2 - 1);
		PointDouble center = new Line(begin, end).getCenter();
		Rectangle rectangle = RelationPointsUtils.toRectangle(center, DRAG_BOX_SIZE/2);
		return rectangle;
	}

	// DRAW METHODS

	public void drawLinesBetweenPoints(DrawHandler drawer) {
		for (Line line : getRelationPointLines()) {
			drawer.drawLine(line);
		}
	}

	public void drawCirclesAndDragBox(DrawHandler drawer) {
		for (PointDouble p : points) {
			drawer.drawCircle(p.x, p.y, POINT_SELECTION_RADIUS);
		}
	
		drawer.drawRectangle(getDragBox());
	}

	public String toAdditionalAttributesString() {
		String returnString = "";
		for (PointDouble p : points) {
			returnString += p.getX() + ";" + p.getY() + ";";
		}
		if (!returnString.isEmpty()) {
			returnString = returnString.substring(0, returnString.length()-1);
		}
		return returnString;
	}

	public void resizeRelationSpaceToMakeTextVisible(double textWidth, double textHeight) {
		if (relation.getRectangle().getWidth() < textWidth) {
			relation.getRectangle().setWidth((int) textWidth);
		}
		if (relation.getRectangle().getHeight() < textHeight) {
			relation.getRectangle().setHeight((int) textHeight);
		}
	}
	
	@Override
	public String toString() {
		return "Relationpoints: " + SharedUtils.listToString(",", points);
	}
}
