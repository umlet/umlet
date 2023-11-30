package com.baselet.element.facet.common;

import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class LinkFacet extends FirstRunKeyValueFacet {
	public static final String LINK = "link";

	public static final LinkFacet INSTANCE = new LinkFacet();

	private LinkFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(LINK, false, "../diagram.uxf", "path to another uxf file (absolute or relative)");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		// only a marker facet to add a context menu entry to open the linked diagram
	}
}
