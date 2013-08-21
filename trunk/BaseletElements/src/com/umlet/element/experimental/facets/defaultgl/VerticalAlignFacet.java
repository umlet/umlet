package com.umlet.element.experimental.facets.defaultgl;

import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.KeyValueGlobalStatelessFacet;

public class VerticalAlignFacet extends KeyValueGlobalStatelessFacet {

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("valign", 
				new ValueInfo(AlignVertical.TOP.toString(), "vertical text alignment"),
				new ValueInfo(AlignVertical.CENTER.toString(), "vertical text alignment"),
				new ValueInfo(AlignVertical.BOTTOM.toString(), "vertical text alignment"));
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.setvAlignGlobally(AlignVertical.valueOf(value.toUpperCase()));
	}

}
