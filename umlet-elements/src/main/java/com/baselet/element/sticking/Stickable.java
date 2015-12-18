package com.baselet.element.sticking;

import java.util.Collection;
import java.util.List;

import com.baselet.control.basics.geom.Rectangle;

public interface Stickable {

	Collection<PointDoubleIndexed> getStickablePoints();

	List<PointDoubleIndexed> movePoints(List<PointChange> changedStickPoints);

	Rectangle getRealRectangle();
}
