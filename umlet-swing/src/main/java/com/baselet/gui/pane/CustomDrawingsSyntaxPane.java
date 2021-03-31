package com.baselet.gui.pane;

import com.baselet.element.interfaces.GridElement;

public class CustomDrawingsSyntaxPane extends AbstractSyntaxPane {

	public CustomDrawingsSyntaxPane(String name) {
		super(name);
	}

	@Override
	public String getPanelAttributes(GridElement e) {
		return e.getCustomDrawingsCode();
	}

}
