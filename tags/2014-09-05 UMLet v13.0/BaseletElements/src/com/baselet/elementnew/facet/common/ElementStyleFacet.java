package com.baselet.elementnew.facet.common;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class ElementStyleFacet extends KeyValueFacet {

	public static ElementStyleFacet INSTANCE = new ElementStyleFacet();

	private ElementStyleFacet() {}

	public enum ElementStyleEnum {
		AUTORESIZE, SIMPLE, NORESIZE, WORDWRAP
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("style",
				new ValueInfo(ElementStyleEnum.AUTORESIZE, "resizes element as text grows"),
				new ValueInfo(ElementStyleEnum.WORDWRAP, "wrap lines at the end of the line"),
				new ValueInfo(ElementStyleEnum.NORESIZE, "disable manual resizing"));
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		state.setElementStyle(ElementStyleEnum.valueOf(value.toUpperCase()));
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
