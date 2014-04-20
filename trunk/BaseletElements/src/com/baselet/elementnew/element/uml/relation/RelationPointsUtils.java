package com.baselet.elementnew.element.uml.relation;

import java.util.List;

import com.baselet.control.SharedConstants;
import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.PointChange;

public class RelationPointsUtils {

	static Rectangle calculateRelationRectangleBasedOnPoints(PointDouble upperLeftCorner, int gridSize, List<PointDoubleHolder> relationPoints) {
		PointDouble elementStart = upperLeftCorner;
		// Calculate new Relation position and size
		Rectangle newSize = createRectangleContainingAllPoints(elementStart, relationPoints);
		if (newSize == null) throw new RuntimeException("This relation has no points: " + relationPoints);
		// scale with zoom factor
		newSize.setBounds(
				newSize.getX() * gridSize / SharedConstants.DEFAULT_GRID_SIZE,
				newSize.getY() * gridSize / SharedConstants.DEFAULT_GRID_SIZE,
				newSize.getWidth() * gridSize / SharedConstants.DEFAULT_GRID_SIZE,
				newSize.getHeight() * gridSize / SharedConstants.DEFAULT_GRID_SIZE);
		// and move to correct place of Relation
		newSize.move(elementStart.getX().intValue(), elementStart.getY().intValue());
		// Realign new size to grid (should not be necessary as long as SELECTCIRCLERADIUS == DefaultGridSize)
		newSize.setLocation(SharedUtils.realignTo(false, newSize.getX(), false, gridSize), SharedUtils.realignTo(false, newSize.getY(), false, gridSize));
		newSize.setSize(SharedUtils.realignTo(false, newSize.getWidth(), true, gridSize), SharedUtils.realignTo(false, newSize.getHeight(), true, gridSize));

		return newSize;
	}

	private static Rectangle createRectangleContainingAllPoints(PointDouble elementStart, List<PointDoubleHolder> relationPoints) {
		Rectangle rectangleContainingAllPoints = null;
		for (PointDoubleHolder p : relationPoints) {
			Rectangle absoluteRectangle = toRectangle(p.getPoint(), RelationPoints.POINT_SELECTION_RADIUS);
			if (rectangleContainingAllPoints == null) {
				rectangleContainingAllPoints = absoluteRectangle;
			} else {
				rectangleContainingAllPoints.merge(absoluteRectangle);
			}
		}
		return rectangleContainingAllPoints;
	}


	static Rectangle toRectangle(PointDouble p, double size) {
		return new Rectangle(p.x-size, p.y-size, size*2, size*2);
	}

	private static Rectangle toCircleRectangle(PointDouble p) {
		return toRectangle(p, RelationPoints.POINT_SELECTION_RADIUS);
	}

	static PointDoubleHolder getRelationPointContaining(Point point, List<PointDoubleHolder> points) {
		for (PointDoubleHolder relationPoint : points) {
			if (toCircleRectangle(relationPoint.getPoint()).contains(point)) {
				return relationPoint;
			}
		}
		return null;
	}

	static void moveRelationPointsOriginToUpperLeftCorner(List<PointDoubleHolder> points) {
		int displacementX = Integer.MAX_VALUE;
		int displacementY = Integer.MAX_VALUE;
		for (PointDoubleHolder p : points) {
			Rectangle r = toCircleRectangle(p.getPoint());
			displacementX = Math.min(displacementX, r.getX());
			displacementY = Math.min(displacementY, r.getY());
		}
		for (PointDoubleHolder pointWithId : points) {
			PointDouble p = pointWithId.getPoint();
			pointWithId.setPoint(new PointDouble(p.getX() - displacementX, p.getY() - displacementY));
			// If points are off the grid they can be realigned here (use the following 2 lines instead of move())
			//			p.setX(SharedUtils.realignTo(true, p.getX()-displacementX, false, SharedConstants.DEFAULT_GRID_SIZE));
			//			p.setY(SharedUtils.realignTo(true, p.getY()-displacementY, false, SharedConstants.DEFAULT_GRID_SIZE));
		}
	}

	public static void applyChangesToPoints(List<PointDoubleHolder> points, List<PointChange> changes) {
		for (PointDoubleHolder p : points) {
			for (PointChange change : changes) {
				if (p.equals(change.getPoint())) {
					p.setPoint(new PointDouble(p.getPoint().getX() + change.getDiffX(), p.getPoint().getY() + change.getDiffY()));
				}
			}
		}
	}
}
