package com.umlet.element.experimental.facet.specific;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.AbstractGlobalKeyValueFacet;

public class TemplateClassFacet extends AbstractGlobalKeyValueFacet {

	public static TemplateClassFacet INSTANCE = new TemplateClassFacet();
	private TemplateClassFacet() {}

	public static final int UPPER_SPACE = 3;
	public static final int LOWER_SPACE = 3;
	public static final int LEFT_SPACE = 6;
	
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("template", new ValueInfo("text", "print template rectangle on top right corner"));
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		double height = drawer.textHeight() + UPPER_SPACE + LOWER_SPACE;
		double width = drawer.textWidth(value) + LEFT_SPACE;
		propConfig.addToRightBuffer(width/2);
		propConfig.setMinTopBuffer(height/2);
		int elemWidth = propConfig.getGridElementSize().width;
		drawer.setDrawDelayed(true);
		drawer.print(value, elemWidth - drawer.getDistanceHorizontalBorderToText(), height-LOWER_SPACE, AlignHorizontal.RIGHT);
		drawer.setDrawDelayed(false);
		propConfig.putFacetResponse(TemplateClassFacet.class, new Rectangle(elemWidth - width, 0.0, width, height));
	}
	
	@Override
	public Priority getPriority() {
		return Priority.HIGH;
	}

}
