package com.baselet.elementnew.facet.specific;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class PackageNameFacet extends KeyValueFacet {

	public static PackageNameFacet INSTANCE = new PackageNameFacet();
	private PackageNameFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("name", new ValueInfo("text", "print package name on top left corner"));
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		state.setFacetResponse(PackageNameFacet.class, value);
	}

}
