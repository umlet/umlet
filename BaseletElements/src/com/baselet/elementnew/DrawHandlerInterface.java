package com.baselet.elementnew;

import com.baselet.control.enumerations.AlignHorizontal;


public interface DrawHandlerInterface {

	void updatePropertyPanel();

	float getZoomFactor();
	
	boolean displaceDrawingByOnePixel();
	
	void resize(double diffw, double diffh, AlignHorizontal alignHorizontal);

}
