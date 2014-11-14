package com.baselet.element.interfaces;

import com.baselet.control.geom.Rectangle;
import com.baselet.diagram.draw.DrawHandler;

public interface Component {
	void setBoundsRect(Rectangle rect);

	Rectangle getBoundsRect();

	void repaintComponent();

	DrawHandler getDrawHandler();

	DrawHandler getMetaDrawHandler();

	void afterModelUpdate();
}
