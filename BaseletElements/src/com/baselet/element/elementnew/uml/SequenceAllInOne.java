package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.ElementStyle;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.FontSizeFacet;
import com.baselet.element.facet.customdrawings.CustomDrawingFacet;
import com.baselet.element.facet.specific.sequence_aio.SequenceAllInOneFacet;

public class SequenceAllInOne extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLSequenceAllInOne;
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		// everything is drawn by the facet
	}

	@Override
	protected Settings createSettings() {
		return new Settings() {

			@Override
			public ElementStyle getElementStyle() {
				// TODO check if AUTO_RESIZE would work (how it can be implemented), and if it would be a good option
				return ElementStyle.SIMPLE;
			}

			@Override
			protected List<Facet> createFacets() {
				return listOf(Settings.BASE, FontSizeFacet.INSTANCE, CustomDrawingFacet.INSTANCE, SequenceAllInOneFacet.INSTANCE);
			}
		};
	}
}
