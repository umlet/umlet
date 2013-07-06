package com.umlet.element.experimental.uml.relation;

import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class ArrowNormal extends Arrow {

	private static final String NORMAL = ">";
	private static final String INVERSE = "<";
	private static final String CLOSED = ">>";
	private static final String INV_CLOSED = "<<";

	@Override
	public boolean checkStart(String line) {
		return line.equals(START+NORMAL) || line.equals(START+INVERSE) || 
				line.equals(START+CLOSED) || line.equals(END+INV_CLOSED) || 
				line.equals(END+NORMAL) || line.equals(END+INVERSE) || 
				line.equals(END+CLOSED) || line.equals(END+INV_CLOSED);
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {
				new AutocompletionText(START+NORMAL, "normal start arrow", isGlobal()),
				new AutocompletionText(START+CLOSED, "closed start arrow", isGlobal()),
				new AutocompletionText(START+INVERSE, "inversed normal start arrow", isGlobal()),
				new AutocompletionText(START+INV_CLOSED, "inversed closed start arrow", isGlobal()),
				new AutocompletionText(END+NORMAL, "normal end arrow", isGlobal()),
				new AutocompletionText(END+CLOSED, "closed end arrow", isGlobal()),
				new AutocompletionText(END+INVERSE, "inversed normal end arrow", isGlobal()),
				new AutocompletionText(END+INV_CLOSED, "inversed closed arrow", isGlobal()),
				};
	}
	

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		RelationPoints relationPoints = getRelationPoints(propConfig);
		boolean start = line.startsWith(START);
		boolean isClosed = line.endsWith(CLOSED) || line.endsWith(INV_CLOSED);
		
		Line lineToDraw = start ? relationPoints.getFirstLine() : relationPoints.getLastLine();
		drawArrowToLine(drawer, lineToDraw, line.startsWith(START), line.endsWith(INVERSE), isClosed);
	}
	
}
