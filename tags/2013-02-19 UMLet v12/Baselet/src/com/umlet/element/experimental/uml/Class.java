package com.umlet.element.experimental.uml;

import com.baselet.control.Constants.AlignHorizontal;
import com.umlet.element.experimental.Id;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsClass;



@Id("UMLClass")
public class Class extends NewGridElement {

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

