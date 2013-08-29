package com.umlet.element.experimental;

import com.baselet.control.enumerations.AlignHorizontal;


public interface DrawHandlerInterface {

	void updatePropertyPanel();

	float getZoomFactor();
	
	boolean displaceDrawingByOnePixel();
	
	void resize(double diffw, double diffh, AlignHorizontal alignHorizontal);

}
