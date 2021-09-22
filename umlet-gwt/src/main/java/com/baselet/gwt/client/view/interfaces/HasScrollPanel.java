package com.baselet.gwt.client.view.interfaces;

import com.baselet.control.basics.geom.Rectangle;
import elemental2.dom.DOMRect;

public interface HasScrollPanel {

	Rectangle getVisibleBounds();

	void moveHorizontalScrollbar(int i);

	void moveVerticalScrollbar(int i);

	int getVerticalScrollPosition();

	int getHorizontalScrollPosition();

	int[] getScrollbarSize();

	DOMRect getBoundedRectCoordinates();
}
