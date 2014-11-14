package com.baselet.element.facet.common;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.ElementStyleEnum;
import com.baselet.element.facet.KeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class ElementStyleFacet extends KeyValueFacet {

	public static final ElementStyleFacet INSTANCE = new ElementStyleFacet();

	private ElementStyleFacet() {}

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
