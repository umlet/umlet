package com.umlet.element.experimental;

import com.baselet.element.GridElement;

public interface DrawHandlerInterface {

	void updateLayer(NewGridElement newGridElement);

	void updatePropertyPanel();

	float getZoomFactor();
	
	boolean displaceDrawingByOnePixel();
	
	GridElement clone(GridElement gridElement);
	
	void Resize(GridElement gridElement, float diffw, float diffh);

}
