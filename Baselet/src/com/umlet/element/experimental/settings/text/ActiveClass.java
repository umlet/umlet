package com.umlet.element.experimental.settings.text;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;

public class ActiveClass implements Facet {

	private static final int SPACING = 6;
	
	@Override
	public boolean checkStart(String line) {
		return line.equals("{active}");
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.addToLeftBuffer(SPACING);
		propConfig.addToRightBuffer(SPACING);
		float[] xLimits = propConfig.getXLimits(propConfig.getyPos());
		drawer.drawLineVertical(xLimits[0]);
		drawer.drawLineVertical(xLimits[1]);
	}

	@Override
	public float getHorizontalSpace() {
		return 0;
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

}
