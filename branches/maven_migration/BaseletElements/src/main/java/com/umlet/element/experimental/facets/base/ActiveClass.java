package com.umlet.element.experimental.facets.base;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalFacet;

public class ActiveClass extends AbstractGlobalFacet {

	public static ActiveClass INSTANCE = new ActiveClass();
	private ActiveClass() {}

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
		// draw delayed vertical line left and right (because at this moment, the size is not fixed - it could be changed by autoresize)
		XValues xLimits = propConfig.getXLimits(propConfig.getyPos());
		drawer.drawLine(xLimits.getLeft(), 0, xLimits.getLeft(), propConfig.getGridElementSize().getHeight());
		drawer.drawLine(xLimits.getRight(), 0, xLimits.getRight(), propConfig.getGridElementSize().getHeight());
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(KEY, "make class active (double left/right border)"));
	}

}
