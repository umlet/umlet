package com.baselet.element.sticking;

import java.util.Collection;
import java.util.List;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.elementnew.element.uml.relation.PointDoubleIndexed;

public interface Stickable {

	Collection<PointDoubleIndexed> getStickablePoints();

	List<PointDoubleIndexed> movePoints(List<PointChange> changedStickPoints);

	Rectangle getRectangle();

	int getGridSize();
}
