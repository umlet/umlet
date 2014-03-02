package com.umlet.element.experimental.facet.common;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.AbstractGlobalKeyValueFacet;

public class LineThicknessFacet extends AbstractGlobalKeyValueFacet {

	public static final double DEFAULT_LINE_THICKNESS = 1.0;
	
	public static LineThicknessFacet INSTANCE = new LineThicknessFacet();
	private LineThicknessFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("lth", false, DEFAULT_LINE_THICKNESS+"", "thickness of lines as decimal number (1.5, 2.0, ...)");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		drawer.setLineThickness(Float.valueOf(value));
	}

	public Priority getPriority() {
		return Priority.HIGHER;
	}

}
