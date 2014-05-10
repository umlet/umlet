package com.baselet.elementnew;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;

public interface Component {
	void setBoundsRect(Rectangle rect);

	Rectangle getBoundsRect();

	void repaintComponent();

	DrawHandler getDrawHandler();

	DrawHandler getMetaDrawHandler();

	void afterModelUpdate();
}
