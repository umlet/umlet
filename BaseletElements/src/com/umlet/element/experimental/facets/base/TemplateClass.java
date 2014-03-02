package com.umlet.element.experimental.facets.base;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class TemplateClass extends AbstractGlobalKeyValueFacet {

	public static TemplateClass INSTANCE = new TemplateClass();
	private TemplateClass() {}

	private static final int UPPER_SPACE = 3;
	private static final int LOWER_SPACE = 3;
	private static final int LEFT_SPACE = 6;
	
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("template", new ValueInfo("text", "print template rectangle on top right corner"));
	}

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		double height = drawer.textHeight() + UPPER_SPACE + LOWER_SPACE;
		double width = drawer.textWidth(value) + LEFT_SPACE;
		Style style = drawer.getCurrentStyle();
		drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
		drawer.setLineType(LineType.DASHED);
		int elemWidth = propConfig.getGridElementSize().width;
		drawer.drawLines(new PointDouble(elemWidth - width, 0), new PointDouble(elemWidth - width, height), new PointDouble(elemWidth, height));
		drawer.print(value, elemWidth - drawer.getDistanceHorizontalBorderToText(), height-LOWER_SPACE, AlignHorizontal.RIGHT);
		drawer.setStyle(style); // reset style to state before manipulations for drawing the template class
		propConfig.addToYPos(height);
	}

}
