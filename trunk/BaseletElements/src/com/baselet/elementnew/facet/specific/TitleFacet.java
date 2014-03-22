package com.baselet.elementnew.facet.specific;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.KeyValueFacet;

public class TitleFacet extends KeyValueFacet {

	public static TitleFacet INSTANCE = new TitleFacet();
	private TitleFacet() {}

	private static final int UPPER_SPACE = 3;
	private static final int LOWER_SPACE = 6;
	
	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("title", new ValueInfo("text", "print title on top left corner"));
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesConfig propConfig) {
		double top = propConfig.getTopBuffer();
		double height = drawer.textHeight() + UPPER_SPACE + LOWER_SPACE + top;
		double corner = height * 0.4;
		double rightSpace = corner * 1.5;
		double lowerLeftSpace = propConfig.getXLimits(height).getLeft();
		double width = drawer.textWidth(value) + rightSpace + lowerLeftSpace;
		Style style = drawer.getCurrentStyle();
		drawer.setDrawDelayed(true);
		drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
		drawer.drawLines(new PointDouble(width, top), new PointDouble(width, height-corner), new PointDouble(width-corner, height), new PointDouble(lowerLeftSpace, height));
		drawer.print(value, lowerLeftSpace + drawer.getDistanceHorizontalBorderToText(), height-LOWER_SPACE, AlignHorizontal.LEFT);
		drawer.setStyle(style); // reset style to state before manipulations for drawing the template class
		drawer.setDrawDelayed(false);
		propConfig.setMinTopBuffer(height);
	}

}
