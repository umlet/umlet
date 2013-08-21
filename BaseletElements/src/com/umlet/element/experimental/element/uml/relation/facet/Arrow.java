package com.umlet.element.experimental.element.uml.relation.facet;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.enumerations.LineType;
import com.baselet.control.enumerations.ValueHolder;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.element.uml.relation.RelationPoints;
import com.umlet.element.experimental.element.uml.relation.SettingsRelation;

public class Arrow extends RelationFacet {

	public static interface ArrowEnd extends ValueHolder {
		public void print(BaseDrawHandler drawer, RelationPoints points);
	}

	private static ArrowEnd LEFT_NORMAL = new ArrowEnd() {
		@Override
		public void print(BaseDrawHandler drawer, RelationPoints points) {
			Arrows.drawArrowToLine(drawer, points.getFirstLine(), true, false, false);
		}
		@Override
		public String getValue() {
			return "<";
		}
	};

	private static ArrowEnd RIGHT_NORMAL = new ArrowEnd() {
		@Override
		public void print(BaseDrawHandler drawer, RelationPoints points) {
			Arrows.drawArrowToLine(drawer, points.getLastLine(), false, false, false);
		}
		@Override
		public String getValue() {
			return ">";
		}
	};

	private static ArrowEnd LEFT_CLOSED = new ArrowEnd() {
		@Override
		public void print(BaseDrawHandler drawer, RelationPoints points) {
			Arrows.drawArrowToLine(drawer, points.getFirstLine(), true, false, true);
		}
		@Override
		public String getValue() {
			return "<<";
		}
	};

	private static ArrowEnd RIGHT_CLOSED = new ArrowEnd() {
		@Override
		public void print(BaseDrawHandler drawer, RelationPoints points) {
			Arrows.drawArrowToLine(drawer, points.getLastLine(), false, false, true);
		}
		@Override
		public String getValue() {
			return ">>";
		}
	};

	private Logger log = Logger.getLogger(Arrow.class);

	/**
	 * all arrowtypes and linetypes to expect (order is important because eg << must be before < to be recognized correctly / also linetype .. must be before .)
	 */
	private static final List<ArrowEnd> LEFT_ARROW_STRINGS = Arrays.asList(LEFT_CLOSED, LEFT_NORMAL);
	private static final List<ArrowEnd> RIGHT_ARROW_STRINGS = Arrays.asList(RIGHT_CLOSED, RIGHT_NORMAL);
	private static final List<LineType> LINE_TYPES = Arrays.asList(LineType.SOLID, LineType.DOTTED, LineType.DASHED, LineType.BOLD);

	private static final String KEY = "lt=";

	@Override
	public boolean checkStart(String line) {
		return (line.startsWith(KEY));
	}

	public RelationPoints getRelationPoints(PropertiesConfig config) {
		return ((SettingsRelation) config.getSettings()).getRelationPoints();
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(
				new AutocompletionText(KEY + "->", "relation with arrow to the right", "R0lGODlhPgALANUAAAAAAP////v7+/f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6Ojo5ubm5eXl4eHh4ODg4CAgHh4eGxsbGhoaGBgYFhYWFRUVEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZZwIBwSCwaj8ikcaFBKJ/QaFQQCVUI0qxWSrCEINuw2JjYeBjjtLjxyRwCFIB8Tq/b7/i8nj56q/9PDyAXBYCGRwsdHAqHjUMGGB8Ojo4DEyISA5SOChgGSEEAOw==")
				);
	}

	private String remainingValue;

	@Override
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig, RelationPoints relationPoints) {
		remainingValue = line.substring(KEY.length());

		ArrowEnd leftArrow = extractPart(LEFT_ARROW_STRINGS, null);
		LineType lineType = extractPart(LINE_TYPES, LineType.SOLID);
		ArrowEnd rightArrow = extractPart(RIGHT_ARROW_STRINGS, null);
		
		drawer.setLineType(lineType);
		if (leftArrow != null) {
			leftArrow.print(drawer, relationPoints);
		}
		if (rightArrow != null) {
			rightArrow.print(drawer, relationPoints);
		}

		log.info("Split Relation " + line + " into following parts: " + getValueNotNull(leftArrow) + " | " + getValueNotNull(lineType) + " | " + getValueNotNull(rightArrow));
	}

	private <T extends ValueHolder> T extractPart(List<T> valueHolderList, T defaultValue) {
		for (T valueHolder : valueHolderList) {
			String s = valueHolder.getValue();
			if (remainingValue.startsWith(s)) {
				remainingValue = remainingValue.substring(s.length());
				return valueHolder;
			}
		}
		return defaultValue;
	}

	private String getValueNotNull(ValueHolder valueHolder) {
		if (valueHolder == null) {
			return "";
		} else {
			return valueHolder.getValue();
		}
	}
}
