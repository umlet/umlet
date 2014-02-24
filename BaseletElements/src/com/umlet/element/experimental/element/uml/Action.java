package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.Settings;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.base.ActionType;
import com.umlet.element.experimental.facets.base.SeparatorLine;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;

public class Action extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLAction;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		// if not type is given, draw an action type as default
		if (!propCfg.getFacetResponse(ActionType.class, false)) {
			ActionType.drawAction(drawer, getRealSize());
		}
	}

	@Override
	protected Settings createSettings() {
		return new Settings() {
			@Override
			public XValues getXValues(double y, int height, int width) {
				return new XValues(0, width);
			}
			@Override
			public AlignVertical getVAlign() {
				return AlignVertical.CENTER;
			}
			@Override
			public AlignHorizontal getHAlign() {
				return AlignHorizontal.CENTER;
			}
			@Override
			public ElementStyleEnum getElementStyle() {
				return ElementStyleEnum.RESIZE;
			}
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLine.INSTANCE, ActionType.INSTANCE);
			}
		};
	}
}
