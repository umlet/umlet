package com.baselet.elementnew.facet.common;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class BackgroundColorFacet extends KeyValueFacet {

	public static BackgroundColorFacet INSTANCE = new BackgroundColorFacet();

	private BackgroundColorFacet() {}

	public static final String KEY = "bg";

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, "red", "background " + ColorOwn.EXAMPLE_TEXT);
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		drawer.setBackgroundColor(value);
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
