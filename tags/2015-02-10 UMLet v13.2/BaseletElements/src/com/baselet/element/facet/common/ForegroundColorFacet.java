package com.baselet.element.facet.common;

import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.Priority;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.facet.GlobalKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class ForegroundColorFacet extends GlobalKeyValueFacet {

	public static final ForegroundColorFacet INSTANCE = new ForegroundColorFacet();

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
