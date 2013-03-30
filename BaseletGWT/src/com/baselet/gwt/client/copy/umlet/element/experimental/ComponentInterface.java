package com.baselet.gwt.client.copy.umlet.element.experimental;

import com.baselet.gwt.client.copy.diagram.draw.BaseDrawHandler;
import com.baselet.gwt.client.copy.diagram.draw.geom.Rectangle;

public interface ComponentInterface {
	void setBoundsRect(Rectangle rect);
	Rectangle getBoundsRect();
	void repaintComponent();
	BaseDrawHandler getDrawHandler();
	BaseDrawHandler getMetaDrawHandler();
	void afterModelUpdate();
}
