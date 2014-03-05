package com.umlet.element.experimental.facets.defaults;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class LayerFacet extends AbstractGlobalKeyValueFacet {

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
			propConfig.putFacetResponse(LayerFacet.class, Integer.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a positive or negative integer");
		}
	}

}
