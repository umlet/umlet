package com.baselet.element.facet.common;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Priority;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.GlobalKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class HorizontalAlignFacet extends GlobalKeyValueFacet {

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
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		state.getAlignment().setHorizontal(true, AlignHorizontal.valueOf(value.toUpperCase()));
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
