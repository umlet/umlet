package com.baselet.diagram.commandnew;

import java.util.List;

import com.baselet.element.GridElement;

public interface CanAddAndRemoveGridElement {
	void addGridElements(List<GridElement> element);

	void removeGridElements(List<GridElement> element);
}
