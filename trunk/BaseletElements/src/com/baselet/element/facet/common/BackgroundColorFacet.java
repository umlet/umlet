package com.baselet.element.facet.common;

import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.Priority;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.facet.KeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class BackgroundColorFacet extends KeyValueFacet {

	public static final BackgroundColorFacet INSTANCE = new BackgroundColorFacet();

	private BackgroundColorFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(FacetConstants.BACKGROUND_COLOR_KEY, false, "red", "background " + ColorOwn.EXAMPLE_TEXT);
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
