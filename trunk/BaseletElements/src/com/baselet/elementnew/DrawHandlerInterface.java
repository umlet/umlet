package com.baselet.elementnew;

import com.baselet.element.sticking.StickableMap;

public interface DrawHandlerInterface {

	void updatePropertyPanel();

	int getGridSize();

	boolean displaceDrawingByOnePixel();

	StickableMap getStickableMap();

	boolean isInitialized();

}
