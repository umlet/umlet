package com.baselet.elementnew.element.uml.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.PointChange;

public class RelationPointHandler implements ResizableObject {

	public static final int DRAG_BOX_SIZE = 10; // size of the box to drag the whole relation
	public static final int POINT_SELECTION_RADIUS = 10; // radius of the selection circle of relation-points
	public static final int NEW_POINT_DISTANCE = 7; // distance from which new points can be dragged away from a relation-line

	/**
	 * Points of this relation (point of origin is the upper left corner of the relation element (not the drawpanel!))
	 */
	private RelationPointList points = new RelationPointList();
	private final Relation relation;

	public RelationPointHandler(Relation relation, RelationPointList points) {
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
		}
		else if (RelationPointHandlerUtils.getRelationPointContaining(point, points) != null) {
			return Selection.RELATION_POINT;
		}
		else if (getLineContaining(point) != null) {
			return Selection.LINE;
		}
		else {
			return Selection.NOTHING;
		}
	}

	private PointDoubleIndexed relationPointOfCurrentDrag = null;

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
		PointDoubleIndexed pointOverRelationPoint = RelationPointHandlerUtils.getRelationPointContaining(point, points);
		if (pointOverRelationPoint != null) {
			relationPointOfCurrentDrag = movePointAndResizeRectangle(pointOverRelationPoint, diffX, diffY);
			return Selection.RELATION_POINT;
		}
		Line lineOnPoint = getLineContaining(point);
		if (lineOnPoint != null) {
			relationPointOfCurrentDrag = points.addPointOnLine(lineOnPoint, SharedUtils.realignToGridRoundToNearest(false, point.x), SharedUtils.realignToGridRoundToNearest(false, point.y));
			relationPointOfCurrentDrag = movePointAndResizeRectangle(relationPointOfCurrentDrag, diffX, diffY);
			return Selection.LINE;
		}
		return Selection.NOTHING;
	}

	private boolean isPointOverDragBox(Point point) {
		return getDragBox().contains(point);
	}

	private Line getLineContaining(Point point) {
		for (Line line : points.getRelationPointLines()) {
			double distanceToPoint = line.getDistanceToPoint(point.toPointDouble());
			if (distanceToPoint < NEW_POINT_DISTANCE) {
				return line;
			}
		}
		return null;
	}

	List<PointDoubleIndexed> movePointAndResizeRectangle(List<PointChange> changedPoints) {
		points.applyChangesToPoints(changedPoints);
		resizeRectAndReposPoints();
		List<PointDoubleIndexed> updatedChangedPoint = new ArrayList<PointDoubleIndexed>();
		for (PointChange c : changedPoints) {
			updatedChangedPoint.add(points.get(c.getIndex()));
		}
		return updatedChangedPoint;
	}

	private PointDoubleIndexed movePointAndResizeRectangle(PointDoubleIndexed point, Integer diffX, Integer diffY) {
		return movePointAndResizeRectangle(Arrays.asList(new PointChange(point.getIndex(), diffX, diffY))).get(0);
	}

	public void resizeRectAndReposPoints() {
		// now rebuild width and height of the relation, based on the new positions of the relation-points
		Rectangle newRect = RelationPointHandlerUtils.calculateRelationRectangleBasedOnPoints(relation.getRectangle().getUpperLeftCorner(), relation.getGridSize(), points);
		relation.setRectangle(newRect);

		// move relation points to their new position (their position is relative to the relation-position)
		points.moveRelationPointsAndTextSpacesByToUpperLeftCorner();
	}

	public boolean removeRelationPointIfOnLineBetweenNeighbourPoints() {
		return points.removeRelationPointIfOnLineBetweenNeighbourPoints();
	}

	// HELPER METHODS

	public Line getFirstLine() {
		return points.getFirstLine();
	}

	public Line getLastLine() {
		return points.getLastLine();
	}

	public Collection<PointDoubleIndexed> getStickablePoints() {
		return points.getStickablePoints();
	}

	public Rectangle getDragBox() {
		return points.getDragBox();
	}

	// DRAW METHODS

	public void drawLinesBetweenPoints(DrawHandler drawer) {
		for (Line line : points.getRelationPointLines()) {
			drawer.drawLine(line);
		}
	}

	public void drawCirclesAndDragBox(DrawHandler drawer) {
		for (RelationPoint p : points.getPointHolders()) {
			drawer.drawCircle(p.getPoint().getX(), p.getPoint().getY(), POINT_SELECTION_RADIUS);
		}
		drawer.drawRectangle(getDragBox());
	}

	public String toAdditionalAttributesString() {
		return points.toAdditionalAttributesString();
	}

	void drawSelectionSpace(DrawHandler drawer) {
		for (RelationPoint rp : points.getPointHolders()) {
			drawer.drawRectangle(rp.getSizeAbsolute());
		}
	}

	public void setTextBox(int index, Rectangle size) {
		size = SharedUtils.realignToGrid(size);
		points.setTextBox(index, size);
	}

	/**
	 * resets all textbox indexes except those which are contained in the excludedList
	 */
	public void resetTextBoxIndexesExcept(Set<Integer> excludedList) {
		Set<Integer> unusedTextBoxIndexes = new HashSet<Integer>(points.getTextBoxIndexes());
		unusedTextBoxIndexes.removeAll(excludedList);
		for (Integer index : unusedTextBoxIndexes) {
			points.setTextBox(index, null);
		}
	}

	@Override
	public void setPointMinSize(int index, Rectangle size) {
		size = SharedUtils.realignToGrid(size);
		points.setSize(index, size);
	}

	@Override
	public void resetPointMinSize(int index) {
		points.setSize(index, RelationPoint.DEFAULT_SIZE);
	}

	@Override
	public String toString() {
		return points.toString();
	}

}
