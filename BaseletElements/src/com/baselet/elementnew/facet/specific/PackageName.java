package com.baselet.elementnew.facet.specific;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class PackageName extends KeyValueFacet {

	public static PackageName INSTANCE = new PackageName();
	private PackageName() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("name", new ValueInfo("text", "print package name on top left corner"));
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		state.setFacetResponse(PackageName.class, value);
	}

}
