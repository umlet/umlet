package com.baselet.diagram;

import com.baselet.element.GridElement;
import com.baselet.element.sticking.StickableMap;

public interface Diagram {
	StickableMap getStickables(GridElement draggedElement);
}
