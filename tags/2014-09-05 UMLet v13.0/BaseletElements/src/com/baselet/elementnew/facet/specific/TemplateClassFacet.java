package com.baselet.elementnew.facet.specific;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.SharedUtils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class TemplateClassFacet extends KeyValueFacet {

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
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		state.setFacetResponse(TemplateClassFacet.class, value);
	}

	private static int round(double val) {
		return SharedUtils.realignToGrid(false, val, true);
	}

	public static List<PointDouble> drawTemplateClass(String templateClassText, DrawHandler drawer, PropertiesParserState state, int height, int width) {
		Rectangle tR = calcTemplateRect(templateClassText, drawer, width);
		int classTopEnd = round(tR.getHeight() / 2);
		int classWidth = width - round(tR.getWidth() / 2);
		PointDouble start = new PointDouble(0, classTopEnd);
		List<PointDouble> p = Arrays.asList(
				start,
				new PointDouble(tR.getX(), classTopEnd),
				new PointDouble(tR.getX(), 0),
				new PointDouble(width, 0),
				new PointDouble(width, tR.getHeight()),
				new PointDouble(classWidth, tR.getHeight()),
				new PointDouble(classWidth, height),
				new PointDouble(0, height),
				start);
		// SET BUFFERS FOR REDUCED CLASS BORDER
		state.setMinTopBuffer(tR.getHeight());
		state.addToRightBuffer(width - classWidth);
		// DRAW BACKGROUND RECT
		Style style = drawer.getStyle();
		drawer.setForegroundColor(ColorOwn.TRANSPARENT);
		drawer.drawLines(p);
		drawer.setStyle(style.cloneFromMe()); // reset style to state before manipulations
		// DRAW RIGHT RECT
		drawer.setLineType(LineType.DASHED);
		drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
		drawer.drawRectangle(tR);
		drawer.setStyle(style); // reset style to state before manipulations
		// DRAW PARTIAL CLASS BORDER
		drawer.drawLines(p.get(1), p.get(0), p.get(7), p.get(6), p.get(5));
		// DRAW TEMPLATE TEXT
		drawer.print(templateClassText, width - drawer.getDistanceBorderToText(), tR.getHeight() - LOWER_SPACE, AlignHorizontal.RIGHT);
		return p;
	}

	private static Rectangle calcTemplateRect(String templateClassText, DrawHandler drawer, int width) {
		double templateHeight = drawer.textHeightMax() + UPPER_SPACE + LOWER_SPACE;
		double templateWidth = drawer.textWidth(templateClassText) + LEFT_SPACE;
		Rectangle tR = new Rectangle(width - templateWidth, 0.0, templateWidth, templateHeight);
		return tR;
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGH;
	}

}
