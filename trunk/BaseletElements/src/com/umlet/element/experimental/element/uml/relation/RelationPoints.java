package com.umlet.element.experimental.element.uml.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;

public class RelationPoints {

	public static final int DRAG_BOX_SIZE = 10; // size of the box to drag the whole relation
	public static final int POINT_SELECTION_RADIUS = 10; // radius of the selection circle of relation-points
	public static final int NEW_POINT_DISTANCE = 5; // distance from which new points can be dragged away from a relation-line

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

	/**
	 * because of the complex selection logic, the other method (which applies changes usually) is reused to make sure the result of this method is correct
	 */
	public Selection getSelection(Point point) {
		return getSelectionAndMaybeApplyChangesHelper(point, null, null, null, true, false);
	}

	public Selection getSelectionAndApplyChanges(Point point, Integer diffX, Integer diffY, Relation relation, boolean firstDrag) {
		return getSelectionAndMaybeApplyChangesHelper(point, diffX, diffY, relation, firstDrag, true);
	}

	private PointDouble relationPointOfCurrentDrag = null;
	private Selection getSelectionAndMaybeApplyChangesHelper(Point point, Integer diffX, Integer diffY, Relation relation, boolean firstDrag, boolean applyChanges) {
		// Special case: if this is not the first drag and a relation-point is currently dragged, it has preference
		// Necessary to avoid changing the currently moved point if moving over another point and to avoid losing the current point if it's a new line point and the mouse is dragged very fast
		if (!firstDrag && relationPointOfCurrentDrag != null) {
			if (applyChanges) {
				movePointAndResizeRectangle(relationPointOfCurrentDrag, diffX, diffY);
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
		for (PointDouble relationPoint : points) {
			if (toCircleRectangle(relationPoint).contains(point)) {
				if (applyChanges) {
					relationPointOfCurrentDrag = relationPoint;
					movePointAndResizeRectangle(relationPointOfCurrentDrag, diffX, diffY);

				}
				return Selection.RELATION_POINT;
			}
		}
		for (Line line : getRelationPointLines()) {
			if (line.getDistanceToPoint(point.toPointDouble()) < NEW_POINT_DISTANCE) {
				if (applyChanges) {
					PointDouble roundedPoint = new PointDouble(SharedUtils.realignToGridRoundToNearest(false, point.x), SharedUtils.realignToGridRoundToNearest(false, point.y));
					points.add(points.indexOf(line.getEnd()), roundedPoint);
					relationPointOfCurrentDrag = roundedPoint;
				}
				return Selection.LINE;
			}
		}
		return Selection.NOTHING;
	}

	void movePointAndResizeRectangle(PointDouble point, Integer diffX, Integer diffY) {
		// move the point
		point.move(diffX, diffY);
		// if there are only 2 points and they would overlap now (therefore the relation would have a size of 0x0px), revert the move
		if (points.size() == 2 && points.get(0).equals(points.get(1))) {
			point.move(-diffX, -diffY);
		}
		// now rebuild width and height of the relation, based on the new positions of the relation-points
		repositionRelationAndPointsBasedOnPoints();
	}

	/**
	 * if a relation-point was dragged and there are more than 2 relation-points and the last dragged relation-point overlaps
	 * a neighbour relation-point, they get merged into one point.
	 * @return relation points have been merged
	 */
	public boolean removeRelationPointOfCurrentDragIfItOverlaps() {
		if (relationPointOfCurrentDrag != null && points.size() > 2) {
			PointDouble lastPoint = points.get(0);
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

	void repositionRelationAndPointsBasedOnPoints() {
		PointDouble elementStart = relation.getRectangle().getUpperLeftCorner();
		// Calculate new Relation position and size
		Rectangle newSize = null;
		for (PointDouble p : points) {
			Rectangle absoluteRectangle = toCircleRectangle(new PointDouble(p.x + elementStart.getX(), p.y + elementStart.getY()));
			if (newSize == null) {
				newSize = absoluteRectangle;
			} else {
				newSize.merge(absoluteRectangle);
			}
		}
		if (newSize == null) throw new RuntimeException("This relation has no points: " + points);
		// Realign new size to grid (should not be necessary as long as SELECTCIRCLERADIUS == DefaultGridSize)
		newSize.setLocation(SharedUtils.realignTo(false, newSize.getX(), false, relation.getGridSize()), SharedUtils.realignTo(false, newSize.getY(), false, relation.getGridSize()));
		newSize.setSize(SharedUtils.realignTo(false, newSize.getWidth(), true, relation.getGridSize()), SharedUtils.realignTo(false, newSize.getHeight(), true, relation.getGridSize()));

		// move relation points to their new position (their position is relative to the relation-position)
		int displacementX = Integer.MAX_VALUE;
		int displacementY = Integer.MAX_VALUE;
		for (PointDouble p : points) {
			Rectangle r = toCircleRectangle(p);
			displacementX = Math.min(displacementX, r.getX());
			displacementY = Math.min(displacementY, r.getY());
		}
		for (PointDouble p : points) {
			// p.move(-displacementX, -displacementY) would be sufficient, but it is realigned to make sure displaced points are corrected here
			p.setX(SharedUtils.realignTo(true, p.getX()-displacementX, false, relation.getGridSize()));
			p.setY(SharedUtils.realignTo(true, p.getY()-displacementY, false, relation.getGridSize()));
		}
		
		relation.setRectangle(newSize);
	}

	private Rectangle toCircleRectangle(PointDouble p) {
		return toRectangle(p, POINT_SELECTION_RADIUS);
	}

	private Rectangle toRectangle(PointDouble p, double size) {
		return new Rectangle(p.x-size, p.y-size, size*2, size*2);
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
		Rectangle rectangle = toRectangle(center, DRAG_BOX_SIZE/2);
		return rectangle;
	}

	// DRAW METHODS

	public void drawLinesBetweenPoints(BaseDrawHandler drawer) {
		for (Line line : getRelationPointLines()) {
			drawer.drawLine(line);
		}
	}

	public static Point normalize(Point p, int pixels) {
		Point ret = new Point();
		double d = Math.sqrt(p.x * p.x + p.y * p.y);
		ret.x = (int) (p.x / d * pixels);
		ret.y = (int) (p.y / d * pixels);
		return ret;
	}

	public void drawPointCircles(BaseDrawHandler drawer) {
		for (PointDouble p : points) {
			drawer.drawCircle(p.x, p.y, POINT_SELECTION_RADIUS-1);
		}
	}

	public void drawDragBox(BaseDrawHandler drawer) {
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
}
