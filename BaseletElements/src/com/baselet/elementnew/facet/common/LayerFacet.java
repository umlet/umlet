package com.baselet.elementnew.facet.common;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.KeyValueFacet;

public class LayerFacet extends KeyValueFacet {

	public static LayerFacet INSTANCE = new LayerFacet();
	private LayerFacet() {}

	public static final String KEY = "layer";
	public static final Integer DEFAULT_VALUE = 0;
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, DEFAULT_VALUE.toString(), "higher layers are shown on top of lowers. (-5, 0(=default), 3,...)");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		try {
			propConfig.setFacetResponse(LayerFacet.class, Integer.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a positive or negative integer");
		}
	}

}
