package com.baselet.diagram.commandnew;

import com.baselet.element.GridElement;

public interface CanAddAndRemoveGridElement {
	void addGridElements(GridElement ... element);

	void removeGridElements(GridElement ... element);
}
