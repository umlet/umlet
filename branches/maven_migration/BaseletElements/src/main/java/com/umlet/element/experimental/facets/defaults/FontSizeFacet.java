package com.umlet.element.experimental.facets.defaults;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class FontSizeFacet extends AbstractGlobalKeyValueFacet {

	public static FontSizeFacet INSTANCE = new FontSizeFacet();
	private FontSizeFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("fontsize", false, "12", "font size as decimal number (12.5, 10.3,...)");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		try {
			drawer.setFontSize(Double.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a decimal number");
		}

	}

	public Priority getPriority() {
		return Priority.HIGH;
	}

}
