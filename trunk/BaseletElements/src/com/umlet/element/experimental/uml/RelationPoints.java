package com.umlet.element.experimental.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;

public class RelationPoints {

	private final int SELECTBOXSIZE = 5;
	private final int SELECTCIRCLERADIUS = 10;
	private final int NEW_POINT_DISTANCE = 5;

	/**
	 * Points of this relation (point of origin is the upper left corner of the relation element (not the drawpanel!))
	 */
	private List<Point> points = new ArrayList<Point>();

	public RelationPoints(List<Point> points) {
		super();
		this.points = points;
	}

	static enum Selection {
		RELATION_POINT,
		DRAG_BOX,
		LINE,
		NOTHING;
	}

	public Selection getSelection(Point point) {
		return getSelectionAndMaybeApplyChanges(point, null, null, null, false);
	}

	Selection getSelectionAndMaybeApplyChanges(Point point, Integer diffX, Integer diffY, Relation relation, boolean applyChanges) {
		if (getDragBox().contains(point)) {
			if (applyChanges) {
				relation.setLocationDifference(diffX, diffY);
			}
			return Selection.DRAG_BOX;
		}
		for (Point relationPoint : points) {
			if (toCircleRectangle(relationPoint).contains(point)) {
				if (applyChanges) {
					relationPoint.move(diffX, diffY);
					relation.setRectangle(repositionRelationAndPointsBasedOnPoints(relation.getRectangle().getUpperLeftCorner()));

				}
				return Selection.RELATION_POINT;
			}
		}
		for (Line line : getRelationPointLines()) {
			if (line.distance(point) < NEW_POINT_DISTANCE) {
				if (applyChanges) {
					points.add(points.indexOf(line.getEnd()), point);
					// currentlyDraggedRelationPoint = mousePosBeforeDragRelative;
				}
				return Selection.LINE;
			}
		}
		return Selection.NOTHING;
	}

	//	/**
	//	 * currently dragged point is stored to make sure the selection is not changed (eg if dragging over another point)
	//	 */
	//	private Point currentlyDraggedRelationPoint = null;
	//
	//	public void drag(int diffX, int diffY, Point mousePosBeforeDragRelative) {
	//
	//		// if a relation-point is currently moved, it has preference
	//		if (currentlyDraggedRelationPoint != null && checkAndMoveRelationPoint(currentlyDraggedRelationPoint, mousePosBeforeDragRelative, diffX, diffY)) {
	//			return;
	//		} else {
	//			currentlyDraggedRelationPoint = null;
	//		}
	//		// otherwise the relation-dragbox is checked
	//		if (currentlyDraggedRelationPoint == null && getDragBox().contains(mousePosBeforeDragRelative)) {
	//			return;
	//		}
	//		// then the other relation points are checked
	//		for (Point relationPoint : points) {
	//			if (checkAndMoveRelationPoint(relationPoint, mousePosBeforeDragRelative, diffX, diffY)) {
	//				return;
	//			}
	//		}
	//		// at last check if a new relation-point should be created
	//		for (Line line : getRelationPointLines()) {
	//			if (line.distance(mousePosBeforeDragRelative) < NEW_POINT_DISTANCE) {
	//				points.add(points.indexOf(line.getEnd()), mousePosBeforeDragRelative);
	//				currentlyDraggedRelationPoint = mousePosBeforeDragRelative;
	//				break;
	//			}
	//		}
	//
	//	}
	//
	//	private boolean checkAndMoveRelationPoint(Point relationPoint, Point mousePosBeforeDrag, int diffX, int diffY) {
	//		if (toCircleRectangle(relationPoint).contains(mousePosBeforeDrag)) {
	//			relationPoint.move(diffX, diffY);
	//			currentlyDraggedRelationPoint = relationPoint;
	//			// now apply the new rectangle size
	//			setRectangle(repositionRelationAndPointsBasedOnPoints(getRectangle().getUpperLeftCorner()));
	//			return true;
	//		}
	//		return false;
	//	}

	Rectangle repositionRelationAndPointsBasedOnPoints(Point elementStart) {
		// Calculate new Relation position and size
		Rectangle newSize = null;
		for (Point p : points) {
			Rectangle absoluteRectangle = toCircleRectangle(new Point(p.x + elementStart.getX(), p.y + elementStart.getY()));
			if (newSize == null) {
				newSize = absoluteRectangle;
			} else {
				newSize.merge(absoluteRectangle);
			}
		}
		// move relation points to their new position (their position is relative to the relation-position)
		int xDisplacement = Integer.MAX_VALUE;
		int yDisplacement = Integer.MAX_VALUE;
		for (Point p : points) {
			Rectangle r = toCircleRectangle(p);
			xDisplacement = Math.min(xDisplacement, r.getX());
			yDisplacement = Math.min(yDisplacement, r.getY());
		}
		for (Point p : points) {
			p.move(-xDisplacement, -yDisplacement);
		}
		return newSize;
	}

	private Rectangle toCircleRectangle(Point p) {
		return toRectangle(p, SELECTCIRCLERADIUS+1);
	}

	private Rectangle toRectangle(Point p, int size) {
		return new Rectangle(p.x-size, p.y-size, size*2, size*2);
	}

	// HELPER METHODS

	private List<Line> getRelationPointLines() {
		List<Line> lines = new ArrayList<Line>();
		for (int i = 1; i < points.size(); i++) {
			lines.add(new Line(points.get(i - 1), points.get(i)));
		}
		return lines;
	}

	private Rectangle getDragBox() {
		Point begin = points.get(points.size() / 2);
		Point end = points.get(points.size() / 2 - 1);
		Point center = new Line(begin, end).getCenter();
		Rectangle rectangle = toRectangle(center, SELECTBOXSIZE);
		return rectangle;
	}

	// DRAW METHODS

	public void drawLinesBetweenPoints(BaseDrawHandler drawer) {
		for (Line line : getRelationPointLines()) {
			drawer.drawLine(line);
		}
	}

	public void drawPointCircles(BaseDrawHandler drawer) {
		for (Point p : points) {
			drawer.drawCircle(p.x, p.y, SELECTCIRCLERADIUS);
		}
	}

	public void drawDragBox(BaseDrawHandler drawer) {
		drawer.drawRectangle(getDragBox());
	}
}
