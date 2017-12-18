package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.enums.ElementId;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.SeparatorLineWithHalignChangeFacet;
import com.baselet.element.facet.specific.StateTypeFacet;
import com.baselet.element.facet.specific.SubStateSymbolFacet;
import com.baselet.element.settings.SettingsManualresizeCenter;

public class State extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLState;
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {}

	@Override
	protected Settings createSettings() {
		return new SettingsManualresizeCenter() {
			@Override
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), SeparatorLineWithHalignChangeFacet.INSTANCE, StateTypeFacet.INSTANCE, SubStateSymbolFacet.INSTANCE);
			}
		};
	}
}
