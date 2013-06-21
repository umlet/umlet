package com.baselet.element;

import java.util.List;

public abstract class Selector {

	public abstract void select(GridElement ... elements);
	public abstract void deselect(GridElement ... elements);
	public abstract boolean isSelected(GridElement element);
	public abstract List<GridElement> getSelectedElements();
	
	public void singleSelect(GridElement element) {
		deselectAll();
		select(element);
	}

	public void deselectAll() {
		deselect(getSelectedElements().toArray(new GridElement[getSelectedElements().size()]));
	}
}
