package com.umlet.element.experimental;

import com.baselet.element.Rectangle;

public interface ComponentInterface {

	void setBoundsRect(Rectangle rect);
	Rectangle getBoundsRect();
	void repaintComponent();
}
