package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.ElementStyle;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.ElementStyleFacet;
import com.baselet.element.facet.specific.HierarchyFacet;

public class Hierarchy extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLHierarchy;
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		state.getDrawer().drawRectangle(0, 0, getRealSize().getWidth(), getRealSize().getHeight());
	}

	@Override
	protected Settings createSettings() {
		return new Settings() {

			@Override
			public ElementStyle getElementStyle() {
				return ElementStyle.SIMPLE;
			}

			@Override
			protected List<Facet> createFacets() {
				return listOf(Settings.MANUALRESIZE, ElementStyleFacet.INSTANCE_AUTORESIZEONLY, HierarchyFacet.INSTANCE);
			}
		};
	}

}
