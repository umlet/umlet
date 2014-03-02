package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.Facet;
import com.umlet.element.experimental.facet.common.SeparatorLineFacet;
import com.umlet.element.experimental.facet.specific.ActionTypeFacet;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsManualresize;

public class Action extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLAction;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		// if not type is given, draw an action type as default
		if (!propCfg.getFacetResponse(ActionTypeFacet.class, false)) {
			ActionTypeFacet.drawAction(drawer, getRealSize());
		}
	}

	@Override
	protected Settings createSettings() {
		return new SettingsManualresize() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLineFacet.INSTANCE_WITH_HALIGN_CHANGE, ActionTypeFacet.INSTANCE);
			}
		};
	}
}
