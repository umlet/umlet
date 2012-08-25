package com.umlet.element.experimental.settings.text;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.helper.XPoints;

public class Helper {

	public static void drawHorizontalLine(float hSpace, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		float linePos = getHLinePos(drawer, propConfig);
		XPoints xPos = propConfig.getXLimits(linePos);
		drawer.drawLine(xPos.getLeft()+1, linePos, xPos.getRight()-1, linePos);
		propConfig.addToYPos(hSpace);
	}
	
	public static float getHLinePos(BaseDrawHandler drawer, PropertiesConfig propConfig) {
		return propConfig.getyPos() - (drawer.textHeight()) + 2;
	}
}
