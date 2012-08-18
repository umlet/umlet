package com.umlet.element.experimental.settings.text;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;

public class InnerClass implements Facet {

	private int depth = 0;
	
	@Override
	public boolean checkStart(String line) {
		if (line.equals("{innerclass")) {
			depth++;
		}
		boolean insideOfInnerClass = depth > 0; // must be calculated here because the line with the closing tag is also inside of the class
		if (line.equals("innerclass}")) {
			if (depth > 0) depth--;
		}
		return insideOfInnerClass;
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		propConfig.setGridElementLeftBuffer(depth * 8);
		propConfig.setGridElementRightBuffer(depth * 8);
	}

	@Override
	public float getHorizontalSpace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean replacesText(String line) {
		return line.equals("{innerclass") || line.equals("innerclass}");
	}

}
