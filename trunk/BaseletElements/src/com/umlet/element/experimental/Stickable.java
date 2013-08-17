package com.umlet.element.experimental;

import java.util.Collection;

import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.element.GridElement;

public interface Stickable extends GridElement {

	Collection<PointDouble> getStickablePoints();
}
