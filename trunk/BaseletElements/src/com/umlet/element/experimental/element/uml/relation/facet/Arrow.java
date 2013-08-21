package com.umlet.element.experimental.element.uml.relation.facet;

import java.util.Arrays;

import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.element.uml.relation.RelationPoints;
import com.umlet.element.experimental.element.uml.relation.SettingsRelation;

public class Arrow extends RelationFacet {

	private static final String KEY = "lt=";

	@Override
	public boolean checkStart(String line) {
		return (line.startsWith(KEY));
	}

	public RelationPoints getRelationPoints(PropertiesConfig config) {
		return ((SettingsRelation) config.getSettings()).getRelationPoints();
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {
				new AutocompletionText(KEY + "->", "relation with arrow to the right", "R0lGODlhPgALANUAAAAAAP////v7+/f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6Ojo5ubm5eXl4eHh4ODg4CAgHh4eGxsbGhoaGBgYFhYWFRUVEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZZwIBwSCwaj8ikcaFBKJ/QaFQQCVUI0qxWSrCEINuw2JjYeBjjtLjxyRwCFIB8Tq/b7/i8nj56q/9PDyAXBYCGRwsdHAqHjUMGGB8Ojo4DEyISA5SOChgGSEEAOw==")
		};
	}

	@Override
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig, RelationPoints relationPoints) {
		String value = line.substring(KEY.length());
		int endIndexOfLeftArrow = getEndIndexOfLeftArrow(value);
		String leftArrow = value.substring(0, endIndexOfLeftArrow);
		String lineType = value.substring(endIndexOfLeftArrow, endIndexOfLeftArrow+1);
		String rightArrow = value.substring(endIndexOfLeftArrow+1, value.length());
		System.out.println("X");
	}

	private int getEndIndexOfLeftArrow(String value) {
		for (LineType lt : Arrays.asList(LineType.SOLID, LineType.DASHED, LineType.DOTTED)) {
			int indexOf = value.indexOf(lt.getValue());
			if (indexOf != -1) return indexOf;
		}
		return -1;
	}
}
