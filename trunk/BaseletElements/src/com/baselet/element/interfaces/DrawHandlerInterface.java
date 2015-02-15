package com.baselet.element.interfaces;

import com.baselet.element.sticking.StickableMap;

public interface DrawHandlerInterface {

	void updatePropertyPanel();

	int getGridSize();

	StickableMap getStickableMap();

	boolean isInitialized();

}
