package com.baselet.element.facet.common;

import com.baselet.control.constants.FacetConstants;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.GlobalKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class LineWidthFacet extends GlobalKeyValueFacet {

	public static final LineWidthFacet INSTANCE = new LineWidthFacet();

	private LineWidthFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("lw", false, FacetConstants.LINE_WIDTH_DEFAULT + "", "linewidth as decimal number (1.5, 2, ...)");
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		drawer.setLineWidth(Float.valueOf(value));
	}

}
