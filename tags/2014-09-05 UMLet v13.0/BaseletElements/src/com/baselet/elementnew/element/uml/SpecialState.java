package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.specific.SpecialStateTypeFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsNoText;

public class SpecialState extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLSpecialState;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		// if not type is given, throw an error
		if (!state.getFacetResponse(SpecialStateTypeFacet.class, false)) { // default is decision
			SpecialStateTypeFacet.drawDecision(drawer, getRealSize().getWidth(), getRealSize().getHeight());
		}
	}

	@Override
	protected Settings createSettings() {
		return new SettingsNoText() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SpecialStateTypeFacet.INSTANCE);
			}
		};
	}
}
