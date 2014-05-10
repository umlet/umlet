package com.baselet.elementnew;

import com.baselet.control.enumerations.AlignHorizontal;

public interface DrawHandlerInterface {

	void updatePropertyPanel();

	int getGridSize();

	boolean displaceDrawingByOnePixel();

	void resize(double diffw, double diffh, AlignHorizontal alignHorizontal);

	boolean isInitialized();

}
