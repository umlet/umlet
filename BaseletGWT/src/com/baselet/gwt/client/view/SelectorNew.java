package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.List;

import com.baselet.element.GridElement;
import com.baselet.element.Selector;

public class SelectorNew extends Selector {
	
	private List<GridElement> selectedElements = new ArrayList<GridElement>();
	
	public GridElement getSingleSelected() {
		if (selectedElements.size() == 1) {
			return selectedElements.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public List<GridElement> getSelectedElements() {
		return selectedElements;
	}

}
