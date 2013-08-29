package com.umlet.element.experimental.facets.defaults;

import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class LineTypeFacet extends AbstractGlobalKeyValueFacet {
	
	public static LineTypeFacet INSTANCE = new LineTypeFacet();
	private LineTypeFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("lt", 
				new ValueInfo(LineType.DASHED, "dashed lines"),
				new ValueInfo(LineType.DOTTED, "dotted lines"),
				new ValueInfo(LineType.BOLD, "bold lines"));
	}
	
	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		drawer.setLineType(value.toUpperCase());
	}

}
