package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.specific.TitleFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualresize;

public class Frame extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualresize() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(TitleFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLFrame;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		drawer.drawRectangle(0, 0, getRealSize().getWidth()-1, getRealSize().getHeight()-1);
	}

}

