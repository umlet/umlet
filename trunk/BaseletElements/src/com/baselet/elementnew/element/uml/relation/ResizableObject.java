package com.baselet.elementnew.element.uml.relation;

import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.Rectangle;

public interface ResizableObject {
	public void resizeToMatchMinSize(DimensionDouble minDimension);

	public void setPointMinSize(int index, Rectangle rectFromCenter);
}
