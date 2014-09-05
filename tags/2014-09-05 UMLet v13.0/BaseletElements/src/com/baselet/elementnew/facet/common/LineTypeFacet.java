package com.baselet.elementnew.facet.common;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class LineTypeFacet extends KeyValueFacet {

	public static LineTypeFacet INSTANCE = new LineTypeFacet();

	private LineTypeFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("lt",
				new ValueInfo(LineType.SOLID.getValue(), "solid lines"),
				new ValueInfo(LineType.DASHED.getValue(), "dashed lines"),
				new ValueInfo(LineType.DOTTED.getValue(), "dotted lines"));
	}

	private static final List<LineType> supportedTypes = Arrays.asList(LineType.SOLID, LineType.DASHED, LineType.DOTTED);

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		LineType lt = null;
		for (LineType s : supportedTypes) {
			if (s.getValue().equals(value)) {
				lt = s;
			}
		}
		if (lt == null)
		{
			throw new RuntimeException(); // will be translated to usage message
		}
		drawer.setLineType(lt);
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
