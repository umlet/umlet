package com.umlet.element.experimental.element.generic;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsText;

public class Text extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsText();
	}
	
	@Override
	public ElementId getId() {
		return ElementId.Text;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
	}

}

