package com.baselet.element;

import java.util.Collection;
import java.util.List;

public abstract class Selector {

	public abstract void select(GridElement ... elements);
	public abstract void deselect(GridElement ... elements);
	public abstract boolean isSelected(GridElement element);
	public abstract List<GridElement> getSelectedElements();
	
	public void selectOnly(GridElement ... elements) {
		deselectAll();
		select(elements);
	}

	public void selectOnly(Collection<GridElement> elements) {
		selectOnly(elements.toArray(new GridElement[elements.size()]));
	}

	public void select(Collection<GridElement> elements) {
		select(elements.toArray(new GridElement[elements.size()]));
	}

	public void deselect(Collection<GridElement> elements) {
		deselect(elements.toArray(new GridElement[elements.size()]));
	}

	public void deselectAll() {
		deselect(getSelectedElements().toArray(new GridElement[getSelectedElements().size()]));
	}
}
