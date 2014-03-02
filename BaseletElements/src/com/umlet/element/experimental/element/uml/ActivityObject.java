package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.Facet;
import com.umlet.element.experimental.facet.common.SeparatorLineFacet;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsManualresize;

public class ActivityObject extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLObject;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		drawer.drawRectangle(0, 0, getRealSize().getWidth()-1, getRealSize().getHeight()-1);
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
