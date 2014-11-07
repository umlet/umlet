package com.baselet.elementnew.facet.common;

import com.baselet.control.constants.FacetConstants;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.elementnew.facet.KeyValueFacet;
import com.baselet.elementnew.facet.PropertiesParserState;

public class ForegroundColorFacet extends KeyValueFacet {

	public static ForegroundColorFacet INSTANCE = new ForegroundColorFacet();

	private ForegroundColorFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(FacetConstants.FOREGROUND_COLOR_KEY, false, "red", "foreground " + ColorOwn.EXAMPLE_TEXT);
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		drawer.setForegroundColor(value);
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
