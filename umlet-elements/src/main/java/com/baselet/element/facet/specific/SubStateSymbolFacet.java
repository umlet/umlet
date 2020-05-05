package com.baselet.element.facet.specific;

import java.util.Locale;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.DrawHandler.Layer;
import com.baselet.diagram.draw.helper.ColorOwnBase;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.element.facet.KeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class SubStateSymbolFacet extends KeyValueFacet {

	public static final SubStateSymbolFacet INSTANCE = new SubStateSymbolFacet();

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
	public void handleValue(String value, PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		drawer.setLayer(Layer.Foreground); // should be always on top of background
		ColorOwnBase prevBackgroundColor = drawer.getBackgroundColor();
		drawer.setBackgroundColor(Theme.getCurrentThemeColor().getColorMap().get(ColorOwnBase.PredefinedColors.TRANSPARENT));
		SubStateSymbolEnum symbol = SubStateSymbolEnum.valueOf(value.toUpperCase(Locale.ENGLISH));
		final double w = state.getGridElementSize().getWidth();
		final double h = state.getGridElementSize().getHeight();
		if (symbol == SubStateSymbolEnum.SUBSTATE) {
			double cW = drawer.getFontSize() * 1.6;
			double cH = cW * 0.4;
			double cR = cW * 0.15;
			double connectorW = cH;
			drawer.drawRectangleRound(w - DIST_RIGHT - cW, h - DIST_BOTTOM - cH, cW, cH, cR);
			drawer.drawRectangleRound(w - DIST_RIGHT - cW - cW - connectorW, h - DIST_BOTTOM - cH, cW, cH, cR);
			drawer.drawLine(w - DIST_RIGHT - cW - connectorW, h - DIST_BOTTOM - cH / 2, w - DIST_RIGHT - cW, h - DIST_BOTTOM - cH / 2);
			drawer.setLayer(Layer.Background);
			drawer.setBackgroundColor(prevBackgroundColor);
		}
	}

}
