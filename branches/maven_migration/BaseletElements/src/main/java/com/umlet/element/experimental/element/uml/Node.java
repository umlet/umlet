package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.base.NodeType;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsNoText;

public class Node extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLNode;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		// if not type is given, throw an error
		if (!propCfg.getFacetResponse(NodeType.class, false)) {
			throw new StyleException("ASDASDDS");
		}
	}

	@Override
	protected Settings createSettings() {
		return new SettingsNoText() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(NodeType.INSTANCE);
			}
		};
	}
}
