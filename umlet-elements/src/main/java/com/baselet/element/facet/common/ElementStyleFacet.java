package com.baselet.element.facet.common;

import java.util.Locale;

import com.baselet.control.enums.ElementStyle;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class ElementStyleFacet extends FirstRunKeyValueFacet {

	private static final ValueInfo NORESIZE_VALUE = new ValueInfo(ElementStyle.NORESIZE, "disable manual resizing");
	private static final ValueInfo WORDWRAP_VALUE = new ValueInfo(ElementStyle.WORDWRAP, "wrap lines at the end of the line");
	private static final ValueInfo AUTORESIZE_VALUE = new ValueInfo(ElementStyle.AUTORESIZE, "resizes element as text grows");

	public static final ElementStyleFacet INSTANCE = new ElementStyleFacet(AUTORESIZE_VALUE, WORDWRAP_VALUE, NORESIZE_VALUE);
	public static final ElementStyleFacet INSTANCE_AUTORESIZEONLY = new ElementStyleFacet(AUTORESIZE_VALUE);

	private final ValueInfo[] valueInfo;

	private ElementStyleFacet(ValueInfo... valueInfo) {
		this.valueInfo = valueInfo;
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("style", valueInfo);
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		state.setElementStyle(ElementStyle.valueOf(value.toUpperCase(Locale.ENGLISH)));
	}

}
