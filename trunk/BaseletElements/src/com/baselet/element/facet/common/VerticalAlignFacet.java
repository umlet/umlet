package com.baselet.element.facet.common;

import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.GlobalKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class VerticalAlignFacet extends GlobalKeyValueFacet {

	public static final VerticalAlignFacet INSTANCE = new VerticalAlignFacet();

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
		state.getAlignment().setVertical(true, AlignVertical.valueOf(value.toUpperCase()));
	}

}
