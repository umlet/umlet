package com.umlet.element.experimental.facets.defaults;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.KeyValueGlobalFacet;

public class HorizontalAlignFacet extends KeyValueGlobalFacet {
	
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

}
