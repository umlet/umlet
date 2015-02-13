package com.baselet.element.facet.common;

import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class FontSizeFacet extends FirstRunKeyValueFacet {

	public static final FontSizeFacet INSTANCE = new FontSizeFacet();

	private FontSizeFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("fontsize", false, "12", "font size as decimal number (12.5, 10.3,...)");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		try {
			state.getDrawer().setFontSize(Double.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a decimal number");
		}

	}

}
