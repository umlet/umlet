package com.umlet.element.experimental.facets.defaults;

import org.apache.log4j.Logger;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class LayerFacet extends AbstractGlobalKeyValueFacet {
	
	public static LayerFacet INSTANCE = new LayerFacet();
	private LayerFacet() {}
	
	private Logger log = Logger.getLogger(LayerFacet.class);

	public static final String KEY = "layer";
	public static final Integer DEFAULT_VALUE = 0;
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, DEFAULT_VALUE.toString(), "higher layers are shown on top of lowers (-5, 0(=default), 3,...)");
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		try {
			propConfig.setLayer(Integer.valueOf(value));
		} catch (NumberFormatException e) {
			log.info("Invalid value: " + value + " - " + KEY + " must be an Integer");
		}
	
	}

}
