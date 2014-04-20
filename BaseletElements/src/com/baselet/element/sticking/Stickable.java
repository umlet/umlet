package com.baselet.element.sticking;

import java.util.Collection;
import java.util.List;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.elementnew.element.uml.relation.PointDoubleHolder;

public interface Stickable {

	Collection<PointDoubleHolder> getStickablePoints();

	List<PointDoubleHolder> movePoints(List<PointChange> changedStickPoints);

	Rectangle getRectangle();
	
	int getGridSize();
}
