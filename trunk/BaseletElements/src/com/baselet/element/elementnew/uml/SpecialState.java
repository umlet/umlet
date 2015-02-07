package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.specific.SpecialStateTypeFacet;
import com.baselet.element.settings.SettingsNoText;

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
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), SpecialStateTypeFacet.INSTANCE);
			}
		};
	}
}
