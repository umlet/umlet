package com.umlet.element.experimental.uml;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsClass;


public class Class extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLClass;
	}
	
	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		drawer.drawRectangle(0, 0, getRealSize().width-1, getRealSize().height-1);
		properties.drawPropertiesText();
	}

	@Override
	public Settings getSettings() {
		return new SettingsClass();
	}
}

