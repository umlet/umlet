package com.umlet.element.experimental.facet.specific;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
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


	public static void drawTemplateClass(BaseDrawHandler drawer, Rectangle tR, int height, int width) {
		// DRAW BACKGROUND RECT
		Style style = drawer.getCurrentStyle();
		drawer.setForegroundColor(ColorOwn.TRANSPARENT);
		PointDouble p1 = new PointDouble(0, tR.getHeight()/2);
		PointDouble p2 = new PointDouble(tR.getX(), tR.getHeight()/2);
		PointDouble p3 = new PointDouble(tR.getX(), 0);
		PointDouble p4 = new PointDouble(width, 0);
		PointDouble p5 = new PointDouble(width, tR.getHeight());
		PointDouble p6 = new PointDouble(width-tR.getWidth()/2, tR.getHeight());
		PointDouble p7 = new PointDouble(width-tR.getWidth()/2, height);
		PointDouble p8 = new PointDouble(0, height);
		drawer.drawLines(p1, p2, p3, p4, p5, p6, p7, p8, p1);
		drawer.setStyle(style.cloneFromMe()); // reset style to state before manipulations
		// DRAW RIGHT RECT
		drawer.setLineType(LineType.DASHED);
		drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
		drawer.drawRectangle(tR);
		drawer.setStyle(style); // reset style to state before manipulations
		// DRAW REST OF CLASS
		drawer.drawLines(p2, p1, p8, p7, p6);
	}
	
	@Override
	public Priority getPriority() {
		return Priority.HIGH;
	}

}
