package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.Facet;
import com.umlet.element.experimental.facet.specific.StateTypeFacet;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsNoText;

public class State extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLState;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		// if not type is given, throw an error
		if (!propCfg.getFacetResponse(StateTypeFacet.class, false)) {
			throw new StyleException("ASDASDDS");
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
