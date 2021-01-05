package com.baselet.element.interfaces;

import java.util.Collection;
import java.util.List;

import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;

public interface Diagram extends HasPanelAttributes, HasGridElements {

	List<Stickable> getStickables();

	StickableMap getStickables(GridElement draggedElement);

	StickableMap getStickables(GridElement draggedElement, Collection<GridElement> excludeList);

	List<GridElement> getGridElementsByLayerLowestToHighest();

	List<GridElement> getGridElementsByLayer(boolean ascending);

	int getZoomLevel();

	void setZoomLevel(int zoomLevel);

}
