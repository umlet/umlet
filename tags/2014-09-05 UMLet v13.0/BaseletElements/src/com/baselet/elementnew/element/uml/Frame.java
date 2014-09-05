package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.facet.common.TextBeforeFirstSeparatorCollectorFacet;
import com.baselet.elementnew.facet.common.TextBeforeFirstSeparatorCollectorFacet.PackageTitleFacetResponse;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualResizeTop;

public class Frame extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualResizeTop() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(TextBeforeFirstSeparatorCollectorFacet.INSTANCE, SeparatorLineFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLFrame;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		drawer.drawRectangle(0, 0, getRealSize().getWidth(), getRealSize().getHeight());

		PackageTitleFacetResponse packageTitleResponse = state.getFacetResponse(TextBeforeFirstSeparatorCollectorFacet.class, null);
		if (packageTitleResponse != null) {
			double top = state.getTopBuffer();
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

			Style style = drawer.getStyle();
			drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
			drawer.drawLines(new PointDouble(width, top), new PointDouble(width, heightOfTitle - corner), new PointDouble(width - corner, heightOfTitle), new PointDouble(lowerLeftSpace, heightOfTitle));
			drawer.setStyle(style); // reset style to state before manipulations for drawing the template class
			state.setMinTopBuffer(heightOfTitle);

			double printHeightIter = textDistanceToTop;
			for (String line : packageTitleResponse.getLines()) {
				printHeightIter += drawer.textHeightMax();
				drawer.print(line, lowerLeftSpace + drawer.getDistanceBorderToText(), printHeightIter, AlignHorizontal.LEFT);
			}
		}
	}
}
