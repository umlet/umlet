package com.baselet.gui.pane;

import com.baselet.element.interfaces.GridElement;

public class PropertiesSyntaxPane extends AbstractSyntaxPane {

	public PropertiesSyntaxPane(String name) {
		super(name);
	}

	@Override
	public String getPanelAttributes(GridElement e) {
		return e.getPanelAttributes();
	}

}
