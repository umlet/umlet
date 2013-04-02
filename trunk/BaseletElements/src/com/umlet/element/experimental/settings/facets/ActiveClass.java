package com.umlet.element.experimental.settings.facets;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class ActiveClass implements Facet {

	private static final String KEY = "{active}";
	
	private static final int SPACING = 6;
	
	@Override
	public boolean checkStart(String line) {
		return line.equals(KEY);
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		//TODO doesn't work inside of an inner class
		propConfig.addToBuffer(SPACING);
		XValues xLimits = propConfig.getXLimits(propConfig.getyPos());
		// draw vertical line left and right
		drawer.drawLine(xLimits.getLeft(), 0, xLimits.getLeft(), propConfig.getGridElementSize().getHeight());
		drawer.drawLine(xLimits.getRight(), 0, xLimits.getRight(), propConfig.getGridElementSize().getHeight());
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {new AutocompletionText(KEY, "make class active (double left/right border)")};
	}

}
