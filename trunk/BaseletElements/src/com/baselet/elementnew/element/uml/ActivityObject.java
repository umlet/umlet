package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualresize;

public class ActivityObject extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLObject;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		drawer.drawRectangle(0, 0, getRealSize().getWidth(), getRealSize().getHeight());
	}

	@Override
	protected Settings createSettings() {
		return new SettingsManualresize() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLineFacet.INSTANCE);
			}
		};
	}
}
