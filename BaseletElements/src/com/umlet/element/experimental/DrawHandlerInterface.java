package com.umlet.element.experimental;


public interface DrawHandlerInterface {

	void updatePropertyPanel();

	float getZoomFactor();
	
	boolean displaceDrawingByOnePixel();
	
	void resize(double diffw, double diffh);

}
