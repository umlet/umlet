package com.umlet.element.experimental.facets.defaults;

import java.util.Arrays;
import java.util.List;

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
				new ValueInfo(LineType.DASHED.getValue(), "dashed lines"),
				new ValueInfo(LineType.DOTTED.getValue(), "dotted lines"),
				new ValueInfo(LineType.BOLD.getValue(), "bold lines"));
	}
	
	private static final List<LineType> supportedTypes = Arrays.asList(LineType.DASHED, LineType.DOTTED, LineType.BOLD);
	
	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		LineType lt = null;
		for (LineType s : supportedTypes) {
			if (s.getValue().equals(value)) lt = s;
		}
		if (lt == null) {
			throw new RuntimeException(); // will be translated to usage message
		}
		drawer.setLineType(lt);
		if (lt == LineType.BOLD) drawer.setLineThickness(2);
	}

	public Priority getPriority() {
		return Priority.HIGH;
	}

}
