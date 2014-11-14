package com.baselet.diagram.commandnew;

import java.util.List;

import com.baselet.control.geom.Rectangle;
import com.baselet.diagram.Diagram;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;

public interface CommandTarget {
	void addGridElements(List<GridElement> element);

	void removeGridElements(List<GridElement> element);

	Selector getSelector();

	Diagram getDiagram();

	Rectangle getVisibleBounds();

	void updatePropertiesPanelWithSelectedElement();
}
