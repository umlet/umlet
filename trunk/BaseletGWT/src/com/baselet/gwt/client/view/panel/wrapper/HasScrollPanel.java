package com.baselet.gwt.client.view.panel.wrapper;

import com.baselet.diagram.draw.geom.Rectangle;

public interface HasScrollPanel {

	Rectangle getVisibleBounds();

	void moveHorizontalScrollbar(int i);

	void moveVerticalScrollbar(int i);

	int getVerticalScrollPosition();

	int getHorizontalScrollPosition();

}
