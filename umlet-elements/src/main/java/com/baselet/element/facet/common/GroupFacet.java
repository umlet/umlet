package com.baselet.element.facet.common;

import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class GroupFacet extends FirstRunKeyValueFacet {

	public static final GroupFacet INSTANCE = new GroupFacet();

	private GroupFacet() {}

	public static final String KEY = "group";

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, "1", "grouped elements are selected at once");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		try {
			state.setFacetResponse(GroupFacet.class, Integer.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a positive or negative integer");
		}
	}
}
