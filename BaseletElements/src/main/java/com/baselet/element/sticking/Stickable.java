package com.baselet.element.sticking;

import java.util.Collection;

import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;

public interface Stickable {

	Collection<PointDouble> getStickablePoints();

	void movePoint(PointDouble pointToMove, int diffX, int diffY);

	Rectangle getRectangle();
}
