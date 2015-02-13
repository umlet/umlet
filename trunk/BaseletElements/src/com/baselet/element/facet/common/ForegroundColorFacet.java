package com.baselet.element.facet.common;

import com.baselet.control.constants.FacetConstants;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class ForegroundColorFacet extends FirstRunKeyValueFacet {

	public static final ForegroundColorFacet INSTANCE = new ForegroundColorFacet();

	private ForegroundColorFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(FacetConstants.FOREGROUND_COLOR_KEY, false, "red", "foreground " + ColorOwn.EXAMPLE_TEXT);
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		state.getDrawer().setForegroundColor(value);
	}

}
