package com.baselet.element.facet.common;

import java.util.Locale;

import com.baselet.control.enums.AlignVertical;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class VerticalAlignFacet extends FirstRunKeyValueFacet {

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
	public void handleValue(String value, PropertiesParserState state) {
		state.getAlignment().setVertical(true, AlignVertical.valueOf(value.toUpperCase(Locale.ENGLISH)));
	}

}
