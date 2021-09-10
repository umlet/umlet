package com.baselet.gwt.client.view.interfaces;

import com.baselet.control.basics.geom.Rectangle;
import elemental2.dom.DOMRect;

public interface HasScrollPanel {

	Rectangle getVisibleBounds();

	void moveHorizontalScrollbar(int i);

	void moveVerticalScrollbar(int i);

	int getVerticalScrollPosition();

	int getMaximumVerticalScrollPosition();

	int getHorizontalScrollPosition();

	int getMaximumHorizontalScrollPosition();

	int[] getScrollbarSize();

	DOMRect getBoundedRectCoordinates();
}
