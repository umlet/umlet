package com.umlet.element.experimental.facets.defaults;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class ElementStyleFacet extends AbstractGlobalKeyValueFacet {
	
	public static ElementStyleFacet INSTANCE = new ElementStyleFacet();
	private ElementStyleFacet() {}

	public enum ElementStyleEnum {AUTORESIZE, RESIZE, NORESIZE, WORDWRAP}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("elementstyle", 
				new ValueInfo(ElementStyleEnum.AUTORESIZE, "resizes element as text grows"),
				new ValueInfo(ElementStyleEnum.WORDWRAP, "wrap lines at the end of the line"),
				new ValueInfo(ElementStyleEnum.NORESIZE, "disable manual resizing"));
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.setElementStyle(ElementStyleEnum.valueOf(value.toUpperCase()));
	}

	public Priority getPriority() {
		return Priority.HIGH;
	}

}
