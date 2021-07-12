package com.baselet.command;

import java.util.List;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;

public interface CommandTarget {
	void addGridElements(List<GridElement> element);

	void addGridElements(List<GridElement> element, boolean recalcSize);

	void removeGridElements(List<GridElement> element);

	Selector getSelector();

	Diagram getDiagram();

	Rectangle getVisibleBounds();

	void updatePropertiesPanelWithSelectedElement();
}
