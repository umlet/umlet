package com.baselet.elementnew.element.uml.relation;

import com.baselet.control.SharedConstants;
import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;

public class RelationPointsUtils {

	static Rectangle calculateRelationRectangleBasedOnPoints(PointDouble upperLeftCorner, int gridSize, PointDoubleHolderList relationPoints) {
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

	private static Rectangle createRectangleContainingAllPoints(PointDouble elementStart, PointDoubleHolderList relationPoints) {
		Rectangle rectangleContainingAllPoints = null;
		for (PointDoubleIndexed p : relationPoints.getPointHolders()) {
			Rectangle absoluteRectangle = toRectangle(p, RelationPoints.POINT_SELECTION_RADIUS);
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

	static Rectangle toCircleRectangle(PointDouble p) {
		return toRectangle(p, RelationPoints.POINT_SELECTION_RADIUS);
	}

	static PointDoubleIndexed getRelationPointContaining(Point point, PointDoubleHolderList points) {
		for (PointDoubleIndexed relationPoint : points.getPointHolders()) {
			if (toCircleRectangle(relationPoint).contains(point)) {
				return relationPoint;
			}
		}
		return null;
	}
}
