package com.umlet.element.experimental.element.uml.relation.facet;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.enumerations.LineType;
import com.baselet.control.enumerations.ValueHolder;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.element.uml.relation.RelationPoints;
import com.umlet.element.experimental.element.uml.relation.SettingsRelation;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class RelationLineTypeFacet extends AbstractGlobalKeyValueFacet {
	
	public static RelationLineTypeFacet INSTANCE = new RelationLineTypeFacet();
	private RelationLineTypeFacet() {}

	private Logger log = Logger.getLogger(RelationLineTypeFacet.class);

	/**
	 * all arrowtypes and linetypes to expect (order is important because eg << must be before < to be recognized correctly / also linetype .. must be before .)
	 */
	private static final List<ArrowEnd> LEFT_ARROW_STRINGS = Arrays.asList(ArrowEnd.LEFT_CLOSED, ArrowEnd.LEFT_NORMAL);
	private static final List<ArrowEnd> RIGHT_ARROW_STRINGS = Arrays.asList(ArrowEnd.RIGHT_CLOSED, ArrowEnd.RIGHT_NORMAL);
	private static final List<LineType> LINE_TYPES = Arrays.asList(LineType.SOLID, LineType.DOTTED, LineType.DASHED, LineType.BOLD);

	public RelationPoints getRelationPoints(PropertiesConfig config) {
		return ((SettingsRelation) config.getSettings()).getRelationPoints();
	}

	private String remainingValue;

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		RelationPoints relationPoints = ((SettingsRelation) propConfig.getSettings()).getRelationPoints();
		remainingValue = value;

		ArrowEnd leftArrow = extractPart(LEFT_ARROW_STRINGS, null);
		LineType lineType = extractPart(LINE_TYPES, LineType.SOLID);
		ArrowEnd rightArrow = extractPart(RIGHT_ARROW_STRINGS, null);
		
		if (leftArrow != null) {
			leftArrow.print(drawer, relationPoints);
		}
		if (rightArrow != null) {
			rightArrow.print(drawer, relationPoints);
		}
		drawer.setLineType(lineType);

		log.debug("Split Relation " + value + " into following parts: " + getValueNotNull(leftArrow) + " | " + getValueNotNull(lineType) + " | " + getValueNotNull(rightArrow));
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

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("lt", 
				new ValueInfo("->", "relation with arrow to the right", "R0lGODlhPgALANUAAAAAAP////v7+/f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6Ojo5ubm5eXl4eHh4ODg4CAgHh4eGxsbGhoaGBgYFhYWFRUVEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZZwIBwSCwaj8ikcaFBKJ/QaFQQCVUI0qxWSrCEINuw2JjYeBjjtLjxyRwCFIB8Tq/b7/i8nj56q/9PDyAXBYCGRwsdHAqHjUMGGB8Ojo4DEyISA5SOChgGSEEAOw=="),
				new ValueInfo("<..>>", "open left, closed right arrow and dotted line", "R0lGODlhPwANAPcAAAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra2xsbG1tbW5ubm9vb3BwcHFxcXJycnNzc3R0dHV1dXZ2dnd3d3h4eHl5eXp6ent7e3x8fH19fX5+fn9/f4CAgIGBgYKCgoODg4SEhIWFhYaGhoeHh4iIiImJiYqKiouLi4yMjI2NjY6Ojo+Pj5CQkJGRkZKSkpOTk5SUlJWVlZaWlpeXl5iYmJmZmZqampubm5ycnJ2dnZ6enp+fn6CgoKGhoaKioqOjo6SkpKWlpaampqenp6ioqKmpqaqqqqurq6ysrK2tra6urq+vr7CwsLGxsbKysrOzs7S0tLW1tba2tre3t7i4uLm5ubq6uru7u7y8vL29vb6+vr+/v8DAwMHBwcLCwsPDw8TExMXFxcbGxsfHx8jIyMnJycrKysvLy8zMzM3Nzc7Ozs/Pz9DQ0NHR0dLS0tPT09TU1NXV1dbW1tfX19jY2NnZ2dra2tvb29zc3N3d3d7e3t/f3+Dg4OHh4eLi4uPj4+Tk5OXl5ebm5ufn5+jo6Onp6erq6uvr6+zs7O3t7e7u7u/v7/Dw8PHx8fLy8vPz8/T09PX19fb29vf39/j4+Pn5+fr6+vv7+/z8/P39/f7+/v///ywAAAAAPwANAAAIwgD/CRxIsKDBgwgTHlynEOE5MNMaSpxYEAyWcRTvHcIw6B7FjxLBIMEAyKPCVSTAnAPJUiGYT+vIkDiFcBoQHMta6kT4UuAyHjyeEVyHBsSonUgN9hx4CgQZht8YANDzDxAAQFWvZsVqlavWrlvDgh379SoYKGX1sMDw7d83LCRuJZ0rcKnAWyiwtB04jAaSa3SRLt3GBMYwhJs4sHkXuOXLd3gwbNqnEDKHS5QbUzzLAQ7jiduo5NQ8cRUTwKRTTwwIADs=")
				);
	}
}
