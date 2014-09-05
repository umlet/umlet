package com.baselet.elementnew.facet.common;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class LineWidthFacet extends KeyValueFacet {

	public static final double DEFAULT_LINE_WIDTH = 1.0;

	public static LineWidthFacet INSTANCE = new LineWidthFacet();

	private LineWidthFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("lw", false, DEFAULT_LINE_WIDTH + "", "linewidth as decimal number (1.5, 2, ...)");
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		drawer.setLineWidth(Float.valueOf(value));
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
