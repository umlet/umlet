package com.baselet.element.facet.specific;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

/**
 * must be in first-run because it manipulates the left buffer which is used by second-run facets
 * must handle values in parsingFinished when drawer-setup is finished
 */
public class UpperRightSymbolFacet extends FirstRunKeyValueFacet {

	public static final UpperRightSymbolFacet INSTANCE = new UpperRightSymbolFacet();

	private UpperRightSymbolFacet() {}

	public enum UpperRightSymbolEnum {
		USECASE, ARTIFACT, COMPONENT
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("symbol",
				new ValueInfo(UpperRightSymbolEnum.USECASE, "draw a use case symbol"),
				new ValueInfo(UpperRightSymbolEnum.ARTIFACT, "draw an artifact symbol"),
				new ValueInfo(UpperRightSymbolEnum.COMPONENT, "draw a component symbol"));
	}

	private static final int DISTANCE = 5;

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		// only act if parsing is finished to make sure DrawHandler-Setup is finished
		UpperRightSymbolEnum symbol = UpperRightSymbolEnum.valueOf(value.toUpperCase(Locale.ENGLISH));
		state.setFacetResponse(UpperRightSymbolFacet.class, symbol);
	}

	public static void drawAndSetBuffer(PropertiesParserState state, UpperRightSymbolEnum symbol) {
		DrawHandler drawer = state.getDrawer();
		ColorOwn prevBackgroundColor = drawer.getBackgroundColor();
		drawer.setBackgroundColor(Theme.getCurrentThemeColor().getColorMap().get(ColorOwn.PredefinedColors.TRANSPARENT));
		double eW = state.getGridElementSize().getWidth();
		double fs = drawer.getFontSize();
		if (symbol == UpperRightSymbolEnum.USECASE) {
			double cW = fs * 2.5;
			double cH = fs;
			drawer.drawEllipse(eW - cW - DISTANCE, DISTANCE, cW, cH);
			state.getBuffer().setTopMin(DISTANCE);
		}
		else if (symbol == UpperRightSymbolEnum.ARTIFACT) {
			double cW = fs * 1.5;
			double cH = fs * 1.8;
			double corner = fs * 0.5;
			List<PointDouble> p = Arrays.asList(
					new PointDouble(eW - cW - DISTANCE, DISTANCE),
					new PointDouble(eW - DISTANCE - corner, DISTANCE),
					new PointDouble(eW - DISTANCE, DISTANCE + corner),
					new PointDouble(eW - DISTANCE, DISTANCE + cH),
					new PointDouble(eW - cW - DISTANCE, DISTANCE + cH));
			PointDouble px = new PointDouble(eW - DISTANCE - corner, DISTANCE + corner);
			drawer.drawLines(p.get(0), p.get(1), p.get(2), p.get(3), p.get(4), p.get(0));
			drawer.drawLines(p.get(1), px, p.get(2));
			state.getBuffer().setTopMin(DISTANCE + fs * 0.3);
		}
		else if (symbol == UpperRightSymbolEnum.COMPONENT) {
			double partHeight = fs * 0.4;
			double nonPartHeight = fs * 0.3;
			double partWidth = partHeight * 2;
			double cH = partHeight * 2 + nonPartHeight * 3;
			double cW = cH * 0.8;
			drawer.drawRectangle(eW - cW - partWidth / 2 - DISTANCE, DISTANCE + nonPartHeight, partWidth, partHeight); // upper small rect
			drawer.drawRectangle(eW - cW - partWidth / 2 - DISTANCE, DISTANCE + partHeight + nonPartHeight * 2, partWidth, partHeight); // lower small rect
			drawer.drawLine(eW - cW - DISTANCE, DISTANCE + partHeight + nonPartHeight, eW - cW - DISTANCE, DISTANCE + partHeight + nonPartHeight * 2); // connection between 2 rects
			drawer.drawLines(Arrays.asList( // draw large rectangle around
					new PointDouble(eW - cW - DISTANCE, DISTANCE + nonPartHeight),
					new PointDouble(eW - cW - DISTANCE, DISTANCE),
					new PointDouble(eW - DISTANCE, DISTANCE),
					new PointDouble(eW - DISTANCE, DISTANCE + cH),
					new PointDouble(eW - cW - DISTANCE, DISTANCE + cH),
					new PointDouble(eW - cW - DISTANCE, DISTANCE + cH - nonPartHeight)));
			state.getBuffer().setTopMin(DISTANCE + fs * 0.3);
		}
		drawer.setBackgroundColor(prevBackgroundColor);
	}

}
