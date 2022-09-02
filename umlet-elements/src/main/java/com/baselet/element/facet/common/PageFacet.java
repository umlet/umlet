package com.baselet.element.facet.common;

import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class PageFacet extends FirstRunKeyValueFacet {

	public static final PageFacet INSTANCE = new PageFacet();

	private PageFacet() {
	}

	public static final String KEY = "page";
	public static final String DEFAULT_VALUE = "";

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, DEFAULT_VALUE.toString(), "Page frame to print");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		// Check restrictions
		state.setPage(value);
	}

}
