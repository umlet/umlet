package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.baselet.element.GridElement;
import com.baselet.element.Selector;

public class SelectorNew implements Selector {
	
	private List<GridElement> selectedElements = new ArrayList<GridElement>();
	
	public void singleSelect(GridElement element) {
		deselectAll();
		select(element);
	}
	
	public void deselectAll() {
		deselect(selectedElements.toArray(new GridElement[selectedElements.size()]));
	}

	public void select(GridElement ... elements) {
		for (GridElement e : elements) {
			selectedElements.add(e);
			e.setSelected(null);
		}
	}

	public void deselect(GridElement ... elements) {
		for (GridElement e : elements) {
			Iterator<GridElement> iter = selectedElements.iterator();
			while (iter.hasNext()) {
				if (iter.next().equals(e)) {
					iter.remove();
					e.setSelected(null);
				}
			}
		}
	}

	public boolean isSelected(GridElement ge) {
		return selectedElements.contains(ge);
	}
	
	public GridElement getSingleSelected() {
		if (selectedElements.size() == 1) {
			return selectedElements.get(0);
		} else {
			return null;
		}
	}

}
