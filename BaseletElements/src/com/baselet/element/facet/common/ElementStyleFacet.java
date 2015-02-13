package com.baselet.element.facet.common;

import com.baselet.control.enums.ElementStyle;
import com.baselet.element.facet.GlobalKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class ElementStyleFacet extends GlobalKeyValueFacet {

	public static final ElementStyleFacet INSTANCE = new ElementStyleFacet();

	private ElementStyleFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("style",
				new ValueInfo(ElementStyle.AUTORESIZE, "resizes element as text grows"),
				new ValueInfo(ElementStyle.WORDWRAP, "wrap lines at the end of the line"),
				new ValueInfo(ElementStyle.NORESIZE, "disable manual resizing"));
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		state.setElementStyle(ElementStyle.valueOf(value.toUpperCase()));
	}

}
