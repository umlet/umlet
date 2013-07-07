package com.umlet.element.experimental;

import com.baselet.element.GridElement;
import com.baselet.element.Selector;

public interface DrawHandlerInterface {

	void updatePropertyPanel();

	float getZoomFactor();
	
	boolean displaceDrawingByOnePixel();
	
	GridElement cloneElement();
	
	void Resize(double diffw, double diffh);

	Selector getSelector();

}
