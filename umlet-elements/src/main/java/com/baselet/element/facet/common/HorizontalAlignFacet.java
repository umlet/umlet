package com.baselet.element.facet.common;

import java.util.Locale;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class HorizontalAlignFacet extends FirstRunKeyValueFacet {

	public static final HorizontalAlignFacet INSTANCE = new HorizontalAlignFacet();

	private HorizontalAlignFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("halign",
				new ValueInfo(AlignHorizontal.LEFT, "horizontal text alignment"),
				new ValueInfo(AlignHorizontal.CENTER, "horizontal text alignment"),
				new ValueInfo(AlignHorizontal.RIGHT, "horizontal text alignment"));
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		state.getAlignment().setHorizontal(true, AlignHorizontal.valueOf(value.toUpperCase(Locale.ENGLISH)));
	}

}
