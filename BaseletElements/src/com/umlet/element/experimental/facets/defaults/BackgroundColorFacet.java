package com.umlet.element.experimental.facets.defaults;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class BackgroundColorFacet extends AbstractGlobalKeyValueFacet {
	
	public static BackgroundColorFacet INSTANCE = new BackgroundColorFacet();
	private BackgroundColorFacet() {}

	public static final String KEY = "bg";
	
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, "red", "background color string (green,...) or code (#3c7a00,...)");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		drawer.setBackgroundColor(value);
	}

}
