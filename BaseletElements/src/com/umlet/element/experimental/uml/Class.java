package com.umlet.element.experimental.uml;

import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsClass;


public class Class extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLClass;
	}
	
	@Override
	public void updateConcreteModel() {
		drawer.drawRectangle(0, 0, getRealSize().width-1, getRealSize().height-1);
		properties.drawPropertiesText();
	}

	@Override
	public Settings getSettings() {
		return new SettingsClass();
	}
}

