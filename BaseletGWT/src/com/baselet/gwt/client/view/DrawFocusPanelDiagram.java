package com.baselet.gwt.client.view;

import com.baselet.control.NewGridElementConstants;
import com.baselet.element.GridElement;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;

public class DrawFocusPanelDiagram extends DrawFocusPanel {

	public DrawFocusPanelDiagram(MainView mainView, PropertiesTextArea propertiesPanel) {
		super(mainView, propertiesPanel);
	}

	@Override
	void doDoubleClickAction(GridElement ge) {
		GridElement e = ge.CloneFromMe();
		e.setLocationDifference(NewGridElementConstants.DEFAULT_GRID_SIZE, NewGridElementConstants.DEFAULT_GRID_SIZE);
		commandInvoker.addElements(this, e);
	}

}
