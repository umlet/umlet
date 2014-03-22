package com.baselet.elementnew.facet.specific;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.KeyValueFacet;

public class PackageName extends KeyValueFacet {

	public static PackageName INSTANCE = new PackageName();
	private PackageName() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("name", new ValueInfo("text", "print package name on top left corner"));
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.setFacetResponse(PackageName.class, value);
	}

}
