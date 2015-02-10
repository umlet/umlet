package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.List;

import com.baselet.element.Selector;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.interfaces.HasGridElements;

public class SelectorNew extends Selector {

	private HasGridElements gridElementProvider;

	public SelectorNew(HasGridElements gridElementProvider) {
		this.gridElementProvider = gridElementProvider;
	}

	public void setGridElementProvider(HasGridElements gridElementProvider) {
		this.gridElementProvider = gridElementProvider;
	}

	private List<GridElement> selectedElements = new ArrayList<GridElement>();

	public GridElement getSingleSelected() {
		if (selectedElements.size() == 1) {
			return selectedElements.get(0);
		}
		else {
			return null;
		}
	}

	@Override
	public List<GridElement> getSelectedElements() {
		return selectedElements;
	}

	@Override
	public List<GridElement> getAllElements() {
		return gridElementProvider.getGridElements();
	}

}
