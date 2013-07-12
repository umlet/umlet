package com.umlet.element.experimental.element.uml.relation.facet;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.element.uml.relation.RelationPoints;

public class LineDescription extends RelationFacet {

	private static final String MESSAGE_START_KEY = "m1";
	private static final String MESSAGE_END_KEY = "m2";
	@Override
	public boolean checkStart(String line) {
		return line.startsWith(MESSAGE_START_KEY + SEP) || line.startsWith(MESSAGE_END_KEY + SEP);
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {
				new AutocompletionText(MESSAGE_START_KEY + SEP, "message at start"),
				new AutocompletionText(MESSAGE_END_KEY + SEP, "message at end"),
				};
	}

	@Override
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig, RelationPoints relationPoints) {
		String[] split = line.split(SEP, -1);
		String key = split[0];
		String text = split[1];
		PointDouble p = null;
		if (!text.isEmpty()) {
			if (key.equals(MESSAGE_START_KEY)) {
				p = relationPoints.getFirstLine().getStart();
			} else if (key.equals(MESSAGE_END_KEY)) {
				p = relationPoints.getLastLine().getEnd();
			}
			drawer.print(text, p, AlignHorizontal.LEFT);
		}
	}

}
