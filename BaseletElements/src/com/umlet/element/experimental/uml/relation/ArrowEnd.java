package com.umlet.element.experimental.uml.relation;

import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.umlet.element.experimental.PropertiesConfig;

public class ArrowEnd extends Arrow {
	
	public void draw(BaseDrawHandler drawer, Line line) {
		drawArrowToLine(drawer, line, false);
	}

	@Override
	public String getKey() {
		return "end=>";
	}
	
	@Override
	public String getDescription() {
		return "draw normal arrow";
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		List<Line> linesToDraw = getLinesFromConfig(propConfig);
		Line lineToDraw = linesToDraw.get(linesToDraw.size()-1);
		drawArrowToLine(drawer, lineToDraw, false);
	}
	
}
