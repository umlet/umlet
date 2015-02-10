package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.SeparatorLineFacet;
import com.baselet.element.facet.common.TextBeforeFirstSeparatorCollectorFacet;
import com.baselet.element.facet.common.TextBeforeFirstSeparatorCollectorFacet.TextBeforeFirstSeparatorCollectorFacetResponse;
import com.baselet.element.settings.SettingsManualResizeTop;

public class Frame extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualResizeTop() {
			@Override
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), TextBeforeFirstSeparatorCollectorFacet.INSTANCE, SeparatorLineFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLFrame;
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		drawer.drawRectangle(0, 0, getRealSize().getWidth(), getRealSize().getHeight());

		TextBeforeFirstSeparatorCollectorFacetResponse packageTitleResponse = state.getFacetResponse(TextBeforeFirstSeparatorCollectorFacet.class, null);
		if (packageTitleResponse != null) {
			double top = state.getBuffer().getTop();
			double textDistanceToTop = drawer.getDistanceBorderToText() + top;
			double heightOfTitle = drawer.getDistanceBorderToText() + textDistanceToTop;
			double width = 0;
			for (String line : packageTitleResponse.getLines()) {
				width = Math.max(width, drawer.textWidth(line));
				heightOfTitle += drawer.textHeightMax();
			}
			double corner = heightOfTitle * 0.4;
			double rightSpace = corner * 1.5;
			double lowerLeftSpace = state.getXLimits(heightOfTitle).getLeft();
			width += rightSpace + lowerLeftSpace;

			Style style = drawer.getStyleClone();
			drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
			drawer.drawLines(new PointDouble(width, top), new PointDouble(width, heightOfTitle - corner), new PointDouble(width - corner, heightOfTitle), new PointDouble(lowerLeftSpace, heightOfTitle));
			drawer.setStyle(style); // reset style to state before manipulations for drawing the template class
			state.getBuffer().setTopMin(heightOfTitle);

			double printHeightIter = textDistanceToTop;
			for (String line : packageTitleResponse.getLines()) {
				printHeightIter += drawer.textHeightMax();
				drawer.print(line, lowerLeftSpace + drawer.getDistanceBorderToText(), printHeightIter, AlignHorizontal.LEFT);
			}
		}
	}
}
