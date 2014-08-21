package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.specific.HierarchyFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualResizeTop;

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
			public List<? extends Facet> createFacets() {
				return Arrays.asList(HierarchyFacet.INSTANCE);
			}
		};
	}

}
