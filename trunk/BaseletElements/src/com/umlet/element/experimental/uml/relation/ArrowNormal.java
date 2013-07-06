package com.umlet.element.experimental.uml.relation;

import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class ArrowNormal extends Arrow {

	private static final String NORMAL = ">";
	private static final String INVERSE = "<";

	@Override
	public boolean checkStart(String line) {
		return line.equals(START+NORMAL) || line.equals(START+INVERSE) || line.equals(END+NORMAL) || line.equals(END+INVERSE);
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {
				new AutocompletionText(START+NORMAL, "normal start arrow", isGlobal()),
				new AutocompletionText(START+INVERSE, "inversed start arrow", isGlobal()),
				new AutocompletionText(END+NORMAL, "normal end arrow", isGlobal()),
				new AutocompletionText(END+INVERSE, "inversed end arrow", isGlobal()),
				};
	}
	

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		List<Line> linesToDraw = getLinesFromConfig(propConfig);
		boolean start = line.startsWith(START);
		
		Line lineToDraw = start ? linesToDraw.get(0) : linesToDraw.get(linesToDraw.size()-1);
		drawArrowToLine(drawer, lineToDraw, line.startsWith(START), line.endsWith(INVERSE));
	}
	
}
