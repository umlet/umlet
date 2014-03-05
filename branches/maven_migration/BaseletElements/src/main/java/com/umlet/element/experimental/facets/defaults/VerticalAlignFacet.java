package com.umlet.element.experimental.facets.defaults;

import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class VerticalAlignFacet extends AbstractGlobalKeyValueFacet {
	
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
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.setvAlignGlobally(AlignVertical.valueOf(value.toUpperCase()));
	}

	public Priority getPriority() {
		return Priority.HIGH;
	}

}
