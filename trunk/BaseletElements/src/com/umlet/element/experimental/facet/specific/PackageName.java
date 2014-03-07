package com.umlet.element.experimental.facet.specific;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.KeyValueFacet;

public class PackageName extends KeyValueFacet {

	public static PackageName INSTANCE = new PackageName();
	private PackageName() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("name", new ValueInfo("text", "print package name on top left corner"));
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.setFacetResponse(PackageName.class, value);
	}

}
