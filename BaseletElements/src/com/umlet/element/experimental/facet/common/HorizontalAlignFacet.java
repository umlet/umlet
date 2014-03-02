package com.umlet.element.experimental.facet.common;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.AbstractGlobalKeyValueFacet;

public class HorizontalAlignFacet extends AbstractGlobalKeyValueFacet {
	
	public static HorizontalAlignFacet INSTANCE = new HorizontalAlignFacet();
	private HorizontalAlignFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("halign", 
				new ValueInfo(AlignHorizontal.LEFT, "horizontal text alignment"),
				new ValueInfo(AlignHorizontal.CENTER, "horizontal text alignment"),
				new ValueInfo(AlignHorizontal.RIGHT, "horizontal text alignment"));
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.sethAlignGlobally(AlignHorizontal.valueOf(value.toUpperCase()));
	}

	public Priority getPriority() {
		return Priority.HIGH;
	}

}
