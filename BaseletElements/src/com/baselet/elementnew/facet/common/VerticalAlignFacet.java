package com.baselet.elementnew.facet.common;

import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class VerticalAlignFacet extends KeyValueFacet {

	public static VerticalAlignFacet INSTANCE = new VerticalAlignFacet();

	private VerticalAlignFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("valign",
				new ValueInfo(AlignVertical.TOP, "vertical text alignment"),
				new ValueInfo(AlignVertical.CENTER, "vertical text alignment"),
				new ValueInfo(AlignVertical.BOTTOM, "vertical text alignment"));
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		state.setvAlignGlobally(AlignVertical.valueOf(value.toUpperCase()));
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
