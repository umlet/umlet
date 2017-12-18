package com.baselet.element.facet.common;

import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class LayerFacet extends FirstRunKeyValueFacet {

	public static final LayerFacet INSTANCE = new LayerFacet();

	private LayerFacet() {}

	public static final String KEY = "layer";
	public static final Integer DEFAULT_VALUE = 0;
	public static final Integer DEFAULT_VALUE_RELATION = 1;

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, DEFAULT_VALUE.toString(), "higher layers are shown on top of lowers. (-5, 0(=default), 3,...)");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		try {
			state.setFacetResponse(LayerFacet.class, Integer.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a positive or negative integer");
		}
	}

}
