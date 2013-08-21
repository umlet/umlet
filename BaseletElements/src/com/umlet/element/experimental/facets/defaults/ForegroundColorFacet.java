package com.umlet.element.experimental.facets.defaults;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class ForegroundColorFacet extends AbstractGlobalKeyValueFacet {
	
	public static ForegroundColorFacet INSTANCE = new ForegroundColorFacet();
	private ForegroundColorFacet() {}

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
