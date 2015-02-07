package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.specific.HierarchyFacet;
import com.baselet.element.settings.SettingsManualResizeTop;

public class Hierarchy extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLHierarchy;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		drawer.drawRectangle(0, 0, getRealSize().width, getRealSize().height);
	}

	@Override
	protected Settings createSettings() {
		return new SettingsManualResizeTop() {
			@Override
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), HierarchyFacet.INSTANCE);
			}
		};
	}

}
