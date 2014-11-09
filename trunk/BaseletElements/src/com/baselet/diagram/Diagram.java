package com.baselet.diagram;

import java.util.Collection;
import java.util.List;

import com.baselet.element.GridElement;
import com.baselet.element.HasPanelAttributes;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;

public interface Diagram extends HasPanelAttributes, HasGridElements {

	public abstract List<Stickable> getStickables();

	public abstract StickableMap getStickables(GridElement draggedElement);

	public abstract StickableMap getStickables(GridElement draggedElement, Collection<GridElement> excludeList);

	public abstract List<GridElement> getGridElementsByLayerLowestToHighest();

	public abstract List<GridElement> getGridElementsByLayer(boolean ascending);

}
