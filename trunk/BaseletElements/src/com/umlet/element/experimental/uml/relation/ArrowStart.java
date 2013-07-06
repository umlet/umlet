package com.umlet.element.experimental.uml.relation;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.umlet.element.experimental.PropertiesConfig;

public class ArrowStart extends Arrow {

	@Override
	public String getKey() {
		return "start=>";
	}
	
	@Override
	public String getDescription() {
		return "draw normal arrow";
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		Line lineToDraw = getLinesFromConfig(propConfig).get(0);
		drawArrowToLine(drawer, lineToDraw, true);
	}
}