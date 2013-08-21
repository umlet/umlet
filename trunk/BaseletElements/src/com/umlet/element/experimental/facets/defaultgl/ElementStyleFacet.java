package com.umlet.element.experimental.facets.defaultgl;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.KeyValueGlobalStatelessFacet;

public class ElementStyleFacet extends KeyValueGlobalStatelessFacet {

	public enum ElementStyleEnum {AUTORESIZE, RESIZE, NORESIZE, WORDWRAP}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("elementstyle", 
				new ValueInfo(ElementStyleEnum.AUTORESIZE.toString(), "resizes element as text grows"),
				new ValueInfo(ElementStyleEnum.WORDWRAP.toString(), "wrap lines at the end of the line"),
				new ValueInfo(ElementStyleEnum.NORESIZE.toString(), "disable manual resizing"));
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.setElementStyle(ElementStyleEnum.valueOf(value.toUpperCase()));
	}

}
