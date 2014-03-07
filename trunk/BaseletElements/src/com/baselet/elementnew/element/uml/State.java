package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.specific.StateTypeFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsNoText;

public class State extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLState;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		// if not type is given, throw an error
		if (!propCfg.getFacetResponse(StateTypeFacet.class, false)) { // default is decision
			StateTypeFacet.drawDecision(drawer, getRealSize().getWidth(), getRealSize().getHeight());
		}
	}

	@Override
	protected Settings createSettings() {
		return new SettingsNoText() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(StateTypeFacet.INSTANCE);
			}
		};
	}
}
