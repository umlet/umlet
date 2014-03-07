package com.baselet.elementnew.facet.common;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.KeyValueFacet;

public class GroupFacet extends KeyValueFacet {

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
			propConfig.setFacetResponse(GroupFacet.class, Integer.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a positive or negative integer");
		}
	}

}
