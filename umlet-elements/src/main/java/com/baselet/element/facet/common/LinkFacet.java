package com.baselet.element.facet.common;

import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class LinkFacet extends FirstRunKeyValueFacet {

	public static final LinkFacet INSTANCE = new LinkFacet();

	private LinkFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("link", false, "../../dummyLink.uxf", "path to a uxf file relative to the path of current file.");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
            /* Empty at first. */
	}
}
