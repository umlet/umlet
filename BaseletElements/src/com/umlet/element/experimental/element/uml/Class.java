package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.base.ActiveClass;
import com.umlet.element.experimental.facets.base.InnerClass;
import com.umlet.element.experimental.facets.base.SeparatorLine;
import com.umlet.element.experimental.facets.base.TemplateClass;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsClass;


public class Class extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsClass() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(new InnerClass(), SeparatorLine.INSTANCE_WITH_HALIGN_CHANGE, ActiveClass.INSTANCE, TemplateClass.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLClass;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		drawer.drawRectangle(0, 0, getRealSize().width-1, getRealSize().height-1);
	}
}

