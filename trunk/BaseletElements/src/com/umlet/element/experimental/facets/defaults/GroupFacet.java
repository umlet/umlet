package com.umlet.element.experimental.facets.defaults;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class GroupFacet extends AbstractGlobalKeyValueFacet {

	public static GroupFacet INSTANCE = new GroupFacet();
	private GroupFacet() {}

	public static final String KEY = "group";
	public static final String DEFAULT_VALUE = "";
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, DEFAULT_VALUE, "grouped elements are selected at once");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.putFacetResponse(GroupFacet.class, value);
	}

}
