package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.enums.ElementId;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.specific.SpecialStateTypeFacet;
import com.baselet.element.settings.SettingsManualresizeCenter;

public class SpecialState extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLSpecialState;
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {}

	@Override
	protected Settings createSettings() {
		return new SettingsManualresizeCenter() {
			@Override
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), SpecialStateTypeFacet.INSTANCE);
			}
		};
	}
}
