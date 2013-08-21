package com.umlet.element.experimental.facets.defaultgl;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.KeyValueGlobalStatelessFacet;

public class ForegroundColorFacet extends KeyValueGlobalStatelessFacet {

	public static final String KEY = "fg";
	
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, "red", "foreground color string (blue,...) or code (#0A37D3,...)");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		drawer.setForegroundColor(value);
	}

}
