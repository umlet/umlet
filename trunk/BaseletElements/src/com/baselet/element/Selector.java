package com.baselet.element;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class Selector {

	private void selectHelper(boolean applyAfterAction, GridElement ... elements) {
		for (GridElement e : elements) {
			if (!getSelectedElements().contains(e)) {
				getSelectedElements().add(e);
				doAfterSelect(e);
				e.getComponent().afterModelUpdate();
			}
		}
		if (applyAfterAction) {
			doAfterSelectionChanged();
		}
	}
	
	private void deselectHelper(boolean applyAfterAction, GridElement ... elements) {
		for (GridElement e : elements) {
			Iterator<GridElement> iter = getSelectedElements().iterator();
			while (iter.hasNext()) {
				if (iter.next().equals(e)) {
					iter.remove();
					doAfterDeselect(e);
					e.getComponent().afterModelUpdate();
				}
			}
		}
		if (applyAfterAction) {
			doAfterSelectionChanged();
		}
	}
	
	public void select(GridElement ... elements) {
		selectHelper(true, elements);
	}
	
	public void deselect(GridElement ... elements) {
		deselectHelper(true, elements);
	}

	public abstract List<GridElement> getSelectedElements();

	public boolean isSelected(GridElement ge) {
		return getSelectedElements().contains(ge);
	}
	
	public boolean isSelectedOnly(GridElement ge) {
		return getSelectedElements().size() == 1 && getSelectedElements().contains(ge);
	}
	
	public void selectOnly(GridElement ... elements) {
		deselectHelper(false, getSelectedElements().toArray(new GridElement[getSelectedElements().size()]));
		selectHelper(true, elements);
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
	
	public void doAfterDeselect(GridElement e) {
		//hook method
	}
	
	public void doAfterSelectionChanged() {
		//hook method
	}
	
	public void doAfterSelect(GridElement e) {
		//hook method
	}
}
