package com.baselet.elementnew.element.uml.relation;

import com.baselet.diagram.draw.geom.Rectangle;

public interface ResizableObject {
	public void setPointMinSize(int index, Rectangle rectFromCenter);

	public void resetPointMinSize(int index);
}
