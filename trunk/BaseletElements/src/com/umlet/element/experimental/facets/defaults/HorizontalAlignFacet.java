package com.umlet.element.experimental.facets.defaults;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.KeyValueGlobalStatelessFacet;

public class HorizontalAlignFacet extends KeyValueGlobalStatelessFacet {

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("halign", 
				new ValueInfo(AlignHorizontal.LEFT.toString(), "horizontal text alignment"),
				new ValueInfo(AlignHorizontal.CENTER.toString(), "horizontal text alignment"),
				new ValueInfo(AlignHorizontal.RIGHT.toString(), "horizontal text alignment"));
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.sethAlignGlobally(AlignHorizontal.valueOf(value.toUpperCase()));
	}

}
