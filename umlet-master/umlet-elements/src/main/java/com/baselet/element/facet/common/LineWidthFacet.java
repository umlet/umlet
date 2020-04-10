package com.baselet.element.facet.common;

import com.baselet.control.constants.FacetConstants;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class LineWidthFacet extends FirstRunKeyValueFacet {

	public static final LineWidthFacet INSTANCE = new LineWidthFacet();

	private LineWidthFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("lw", false, FacetConstants.LINE_WIDTH_DEFAULT + "", "linewidth as decimal number (1.5, 2, ...)");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		state.getDrawer().setLineWidth(Float.valueOf(value));
	}

}
