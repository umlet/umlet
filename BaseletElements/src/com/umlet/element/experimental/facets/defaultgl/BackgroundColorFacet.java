package com.umlet.element.experimental.facets.defaultgl;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.KeyValueGlobalStatelessFacet;

public class BackgroundColorFacet extends KeyValueGlobalStatelessFacet {

	public static final String KEY = "bg";
	
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, "red", "background color string (green,...) or code (#3c7a00,...)");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		drawer.setBackgroundColor(value);
	}

}
