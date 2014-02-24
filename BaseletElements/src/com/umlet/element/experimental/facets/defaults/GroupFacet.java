package com.umlet.element.experimental.facets.defaults;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class GroupFacet extends AbstractGlobalKeyValueFacet {

	public static GroupFacet INSTANCE = new GroupFacet();
	private GroupFacet() {}

	public static final String KEY = "group";
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, "1", "grouped elements are selected at once");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		try {
			propConfig.putFacetResponse(GroupFacet.class, Integer.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a positive or negative integer");
		}
	}

}
