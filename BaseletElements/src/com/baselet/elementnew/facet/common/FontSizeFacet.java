package com.baselet.elementnew.facet.common;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class FontSizeFacet extends KeyValueFacet {

	public static FontSizeFacet INSTANCE = new FontSizeFacet();

	private FontSizeFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("fontsize", false, "12", "font size as decimal number (12.5, 10.3,...)");
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		try {
			drawer.setFontSize(Double.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a decimal number");
		}

	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
