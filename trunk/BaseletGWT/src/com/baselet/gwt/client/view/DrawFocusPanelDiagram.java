package com.baselet.gwt.client.view;

import com.baselet.diagram.commandnew.CanAddAndRemoveGridElement;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;

public class DrawFocusPanelDiagram extends DrawFocusPanel {

	public DrawFocusPanelDiagram(MainView mainView, PropertiesTextArea propertiesPanel) {
		super(mainView, propertiesPanel);
	}

	@Override
	CanAddAndRemoveGridElement getDoubleclickTarget() {
		return this;
	}

}
