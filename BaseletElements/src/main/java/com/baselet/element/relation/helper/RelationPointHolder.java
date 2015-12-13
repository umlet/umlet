package com.baselet.element.relation.helper;

import com.baselet.control.basics.geom.Rectangle;

public interface RelationPointHolder {
	Rectangle getRectangle();

	void setRectangle(Rectangle rect);

	int getGridSize();
}
