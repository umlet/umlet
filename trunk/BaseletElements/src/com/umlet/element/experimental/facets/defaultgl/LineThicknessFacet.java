package com.umlet.element.experimental.facets.defaultgl;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.KeyValueGlobalStatelessFacet;

public class LineThicknessFacet extends KeyValueGlobalStatelessFacet {

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("lth", "1.0", "thickness of lines (1.5, 2.0, ...)");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		drawer.setLineThickness(Float.valueOf(value));
	}

}
