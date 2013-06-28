package com.baselet.gwt.client.view.widgets;

import com.baselet.element.GridElement;

public class PropertiesTextArea extends OwnTextArea {

	private GridElement gridElement;
	
	public void setGridElement(GridElement gridElement) {
		this.gridElement = gridElement;
		this.setValue(gridElement.getPanelAttributes());
	}
	
	public GridElement getGridElement() {
		return gridElement;
	}
}
