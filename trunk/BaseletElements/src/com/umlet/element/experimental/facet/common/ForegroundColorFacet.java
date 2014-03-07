package com.umlet.element.experimental.facet.common;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.KeyValueFacet;

public class ForegroundColorFacet extends KeyValueFacet {
	
	public static ForegroundColorFacet INSTANCE = new ForegroundColorFacet();
	private ForegroundColorFacet() {}

	public static final String KEY = "fg";
	
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, "red", "foreground " + ColorOwn.EXAMPLE_TEXT);
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		drawer.setForegroundColor(value);
	}

	public Priority getPriority() {
		return Priority.HIGHER;
	}

}
