package com.umlet.element.experimental.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.helpers.AbsoluteTimeDateFormat;

import com.baselet.control.NewGridElementConstants;
import com.baselet.control.SharedUtils;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;

public class RelationPoints {

	private final int DRAG_BOX_SIZE = 10; // size of the box to drag the whole relation
	private final int POINT_SELECTION_RADIUS = 10; // radius of the selection circle of relation-points
	private final int NEW_POINT_DISTANCE = 5; // distance from which new points can be dragged away from a relation-line

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

	/**
	 * because of the complex selection logic, the other method (which applies changes usually) is reused to make sure the result of this method is correct
	 */
	public Selection getSelection(Point point) {
		return getSelectionAndMaybeApplyChangesHelper(point, null, null, null, 0, true, false);
	}

	public Selection getSelectionAndApplyChanges(Point point, Integer diffX, Integer diffY, Relation relation, int gridSize, boolean firstDrag) {
		return getSelectionAndMaybeApplyChangesHelper(point, diffX, diffY, relation, gridSize, firstDrag, true);
	}

	private Point relationPointOfCurrentDrag = null;
	private Selection getSelectionAndMaybeApplyChangesHelper(Point point, Integer diffX, Integer diffY, Relation relation, int gridSize, boolean firstDrag, boolean applyChanges) {
		// Special case: if this is not the first drag and a relation-point is currently dragged, it has preference
		// Necessary to avoid changing the currently moved point if moving over another point and to avoid losing the current point if it's a new line point and the mouse is dragged very fast
		if (!firstDrag && relationPointOfCurrentDrag != null) {
			if (applyChanges) {
				movePointAndResizeRectangle(relationPointOfCurrentDrag, diffX, diffY, relation, gridSize);
			}
			return Selection.RELATION_POINT;
		}

		// If the special case doesn't apply, forget the relationPointOfFirstDrag, because its a new first drag
		relationPointOfCurrentDrag = null;
		if (getDragBox().contains(point)) {
			if (applyChanges) {
				relation.setLocationDifference(diffX, diffY);
			}
			return Selection.DRAG_BOX;
		}
		for (Point relationPoint : points) {
			if (toCircleRectangle(relationPoint).contains(point)) {
				if (applyChanges) {
					relationPointOfCurrentDrag = relationPoint;
					movePointAndResizeRectangle(relationPointOfCurrentDrag, diffX, diffY, relation, gridSize);

				}
				return Selection.RELATION_POINT;
			}
		}
		for (Line line : getRelationPointLines()) {
			if (line.distance(point) < NEW_POINT_DISTANCE) {
				if (applyChanges) {
					Point roundedPoint = new Point(SharedUtils.realignToGrid(false, point.x), SharedUtils.realignToGrid(false, point.y));
					points.add(points.indexOf(line.getEnd()), roundedPoint);
					relationPointOfCurrentDrag = roundedPoint;
				}
				return Selection.LINE;
			}
		}
		return Selection.NOTHING;
	}

	private void movePointAndResizeRectangle(Point point, Integer diffX, Integer diffY, Relation relation, int gridSize) {
		// move the point
		point.move(diffX, diffY);
		// if there are only 2 points and they would overlap now (therefore the relation would have a size of 0x0px), revert the move
		if (points.size() == 2 && points.get(0).equals(points.get(1))) {
			point.move(-diffX, -diffY);
		}
		// now rebuild width and height of the relation, based on the new positions of the relation-points
		relation.setRectangle(repositionRelationAndPointsBasedOnPoints(relation.getRectangle().getUpperLeftCorner(), gridSize));
	}

	/**
	 * if a relation-point was dragged and there are more than 2 relation-points and the last dragged relation-point overlaps
	 * a neighbour relation-point, they get merged into one point.
	 * @return relation points have been merged
	 */
	public boolean removeRelationPointOfCurrentDragIfItOverlaps() {
		if (relationPointOfCurrentDrag != null && points.size() > 2) {
			Point lastPoint = points.get(0);
			for (int i = 1; i < points.size(); i++) {
				if (points.get(i).equals(lastPoint)) {
					points.remove(i);
					return true;
				} else {
					lastPoint = points.get(i);
				}
			}
		}
		return false;
	}

	Rectangle repositionRelationAndPointsBasedOnPoints(Point elementStart, int gridSize) {
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
		// Realign new size to grid (should not be necessary as long as SELECTCIRCLERADIUS == DefaultGridSize
		newSize.setLocation(SharedUtils.realignTo(false, newSize.getX(), false, gridSize), SharedUtils.realignTo(false, newSize.getY(), false, gridSize));
		newSize.setSize(SharedUtils.realignTo(false, newSize.getWidth(), true, gridSize), SharedUtils.realignTo(false, newSize.getHeight(), true, gridSize));

		// move relation points to their new position (their position is relative to the relation-position)
		int displacementX = Integer.MAX_VALUE;
		int displacementY = Integer.MAX_VALUE;
		for (Point p : points) {
			Rectangle r = toCircleRectangle(p);
			displacementX = Math.min(displacementX, r.getX());
			displacementY = Math.min(displacementY, r.getY());
		}
		for (Point p : points) {
			// p.move(-displacementX, -displacementY) would be sufficient, but it is realigned to make sure displaced points are corrected here
			p.setX(SharedUtils.realignTo(true, p.getX()-displacementX, false, gridSize));
			p.setY(SharedUtils.realignTo(true, p.getY()-displacementY, false, gridSize));
		}
		return newSize;
	}

	private Rectangle toCircleRectangle(Point p) {
		return toRectangle(p, POINT_SELECTION_RADIUS);
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
		Rectangle rectangle = toRectangle(center, DRAG_BOX_SIZE/2);
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
			drawer.drawCircle(p.x, p.y, POINT_SELECTION_RADIUS-1);
		}
	}

	public void drawDragBox(BaseDrawHandler drawer) {
		drawer.drawRectangle(getDragBox());
	}

	public String toAdditionalAttributesString() {
		String returnString = "";
		for (Point p : points) {
			returnString += p.getX() + ";" + p.getY() + ";";
		}
		if (!returnString.isEmpty()) {
			returnString = returnString.substring(0, returnString.length()-1);
		}
		return returnString;
	}
}
