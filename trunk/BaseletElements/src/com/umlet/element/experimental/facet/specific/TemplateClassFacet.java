package com.umlet.element.experimental.facet.specific;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.SharedUtils;
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
		propConfig.addToRightBuffer(round(width/2));
		propConfig.setMinTopBuffer(round(height/2));
		int elemWidth = propConfig.getGridElementSize().width;
		drawer.setDrawDelayed(true);
		drawer.print(value, elemWidth - drawer.getDistanceHorizontalBorderToText(), height-LOWER_SPACE, AlignHorizontal.RIGHT);
		drawer.setDrawDelayed(false);
		propConfig.setFacetResponse(TemplateClassFacet.class, new Rectangle(elemWidth - width, 0.0, width, height));
	}
	
	private static int round(double val) {
		return SharedUtils.realignToGrid(false, val, true);
	}


	public static List<PointDouble> drawTemplateClass(BaseDrawHandler drawer, Rectangle tR, int height, int width) {
		Style style = drawer.getCurrentStyle();
		drawer.setForegroundColor(ColorOwn.TRANSPARENT);
		int classTopEnd = round(tR.getHeight()/2);
		int classWidth = width-round(tR.getWidth()/2);
		List<PointDouble> p = Arrays.asList(
				new PointDouble(0, classTopEnd),
				new PointDouble(tR.getX(), classTopEnd),
				new PointDouble(tR.getX(), 0),
				new PointDouble(width, 0),
				new PointDouble(width, tR.getHeight()),
				new PointDouble(classWidth, tR.getHeight()),
				new PointDouble(classWidth, height),
				new PointDouble(0, height));
		// DRAW BACKGROUND RECT
		int i = 0;
		drawer.drawLines(p.get(i++), p.get(i++), p.get(i++), p.get(i++), p.get(i++), p.get(i++), p.get(i++), p.get(i++), p.get(0));
		drawer.setStyle(style.cloneFromMe()); // reset style to state before manipulations
		// DRAW RIGHT RECT
		drawer.setLineType(LineType.DASHED);
		drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
		drawer.drawRectangle(tR);
		drawer.setStyle(style); // reset style to state before manipulations
		// DRAW PARTIAL CLASS BORDER
		drawer.drawLines(p.get(1), p.get(0), p.get(7), p.get(6), p.get(5));
		return p;
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGH;
	}

}
