package com.baselet.gwt.client.view.interfaces;

import com.baselet.control.basics.geom.Rectangle;

public interface HasScrollPanel {

	Rectangle getVisibleBounds();

	void moveHorizontalScrollbar(int i);

	void moveVerticalScrollbar(int i);

	int getVerticalScrollPosition();

	int getHorizontalScrollPosition();

}
