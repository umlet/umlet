package com.umlet.element.experimental;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;

public interface Component {
	void setBoundsRect(Rectangle rect);
	Rectangle getBoundsRect();
	void repaintComponent();
	BaseDrawHandler getDrawHandler();
	BaseDrawHandler getMetaDrawHandler();
	void afterModelUpdate();
}
