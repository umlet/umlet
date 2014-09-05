package com.baselet.elementnew.facet.specific;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class SubStateSymbolFacet extends KeyValueFacet {

	public static SubStateSymbolFacet INSTANCE = new SubStateSymbolFacet();

	private SubStateSymbolFacet() {}

	private enum SubStateSymbolEnum {
		SUBSTATE
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("symbol",
				new ValueInfo(SubStateSymbolEnum.SUBSTATE, "draw a substate symbol in the lower right corner"));
	}

	private static final int DIST_RIGHT = 15;
	private static final int DIST_BOTTOM = 5;

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		drawer.setDrawDelayed(true);
		ColorOwn prevBackgroundColor = drawer.getStyle().getBackgroundColor();
		drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
		SubStateSymbolEnum symbol = SubStateSymbolEnum.valueOf(value.toUpperCase());
		final double w = state.getGridElementSize().getWidth();
		final double h = state.getGridElementSize().getHeight();
		if (symbol == SubStateSymbolEnum.SUBSTATE) {
			double cW = drawer.getStyle().getFontSize() * 1.6;
			double cH = cW * 0.4;
			double cR = cW * 0.15;
			double connectorW = cH;
			drawer.drawRectangleRound(w - DIST_RIGHT - cW, h - DIST_BOTTOM - cH, cW, cH, cR);
			drawer.drawRectangleRound(w - DIST_RIGHT - cW - cW - connectorW, h - DIST_BOTTOM - cH, cW, cH, cR);
			drawer.drawLine(w - DIST_RIGHT - cW - connectorW, h - DIST_BOTTOM - cH / 2, w - DIST_RIGHT - cW, h - DIST_BOTTOM - cH / 2);
			drawer.setDrawDelayed(false);
			drawer.setBackgroundColor(prevBackgroundColor);
		}
	}

}
