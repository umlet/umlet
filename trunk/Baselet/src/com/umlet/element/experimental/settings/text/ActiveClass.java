package com.umlet.element.experimental.settings.text;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.helper.XPoints;

public class ActiveClass implements Facet {

	private static final int SPACING = 6;
	
	@Override
	public boolean checkStart(String line) {
		return line.equals("{active}");
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.addToBuffer(SPACING);
		XPoints xLimits = propConfig.getXLimits(propConfig.getyPos());
		drawer.drawLineVertical(xLimits.getLeft());
		drawer.drawLineVertical(xLimits.getRight());
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

}
