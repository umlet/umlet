package com.baselet.elementnew.facet.relation;

import com.baselet.control.geom.Rectangle;

public interface ResizableObject {
	public void setPointMinSize(int index, Rectangle rectFromCenter);

	public void resetPointMinSize(int index);
}
