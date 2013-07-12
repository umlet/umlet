package com.umlet.element.experimental;

import com.baselet.element.GridElement;

public interface DrawHandlerInterface {

	void updatePropertyPanel();

	float getZoomFactor();
	
	boolean displaceDrawingByOnePixel();
	
	GridElement cloneElement();
	
	void Resize(double diffw, double diffh);

}
