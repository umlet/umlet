package com.baselet.elementnew.facet.relation;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.SharedUtils;
import com.baselet.control.enumerations.LineType;
import com.baselet.control.enumerations.RegexValueHolder;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.element.uml.relation.PointDoubleIndexed;
import com.baselet.elementnew.element.uml.relation.RelationPoints;
import com.baselet.elementnew.element.uml.relation.ResizableObject;
import com.baselet.elementnew.element.uml.relation.SettingsRelation;
import com.baselet.elementnew.facet.KeyValueFacet;

public class RelationLineTypeFacet extends KeyValueFacet {

	private static class Match<T extends RegexValueHolder> {
		private final String text;
		private final T type;

		public Match(String matchedText, T matchedObject) {
			super();
			this.text = matchedText;
			this.type = matchedObject;
		}

	}

	public static RelationLineTypeFacet INSTANCE = new RelationLineTypeFacet();

	private RelationLineTypeFacet() {}

	private final Logger log = Logger.getLogger(RelationLineTypeFacet.class);

	/**
	 * all arrowtypes and linetypes to expect (order is important because eg << must be before < to be recognized correctly, therefore there are 2 shared lists. also linetype .. must be before .)
	 */
	private static final List<ArrowEnd> SHARED_ARROW_STRINGS_BEFORE = Arrays.asList(ArrowEnd.CIRCLE_CROSS, ArrowEnd.CIRCLE);
	private static final List<ArrowEnd> SHARED_ARROW_STRINGS_AFTER = Arrays.asList(ArrowEnd.BOX_EMPTY, ArrowEnd.BOX_DOWN_ARROW, ArrowEnd.BOX_LEFT_ARROW, ArrowEnd.BOX_RIGHT_ARROW, ArrowEnd.BOX_UP_ARROW, ArrowEnd.BOX_EQUALS, ArrowEnd.BOX_TEXT);
	private static final List<ArrowEnd> LEFT_ARROW_STRINGS = SharedUtils.mergeLists(SHARED_ARROW_STRINGS_BEFORE, Arrays.asList(ArrowEnd.LEFT_FILLED_CLOSED, ArrowEnd.LEFT_FILLED_DIAMOND, ArrowEnd.LEFT_DIAMOND, ArrowEnd.LEFT_CLOSED, ArrowEnd.LEFT_NORMAL, ArrowEnd.LEFT_INTERFACE_OPEN), SHARED_ARROW_STRINGS_AFTER);
	private static final List<ArrowEnd> RIGHT_ARROW_STRINGS = SharedUtils.mergeLists(SHARED_ARROW_STRINGS_BEFORE, Arrays.asList(ArrowEnd.RIGHT_FILLED_CLOSED, ArrowEnd.RIGHT_FILLED_DIAMOND, ArrowEnd.RIGHT_DIAMOND, ArrowEnd.RIGHT_CLOSED, ArrowEnd.RIGHT_NORMAL, ArrowEnd.RIGHT_INTERFACE_OPEN), SHARED_ARROW_STRINGS_AFTER);
	private static final List<LineType> LINE_TYPES = Arrays.asList(LineType.SOLID, LineType.DOTTED, LineType.DASHED);

	public RelationPoints getRelationPoints(PropertiesParserState config) {
		return ((SettingsRelation) config.getSettings()).getRelationPoints();
	}

	private String remainingValue;

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		RelationPoints relationPoints = ((SettingsRelation) state.getSettings()).getRelationPoints();
		remainingValue = value;

		Match<ArrowEnd> leftArrow = extractPart(LEFT_ARROW_STRINGS, null);
		Match<LineType> lineType = extractPart(LINE_TYPES, LineType.SOLID);
		Match<ArrowEnd> rightArrow = extractPart(RIGHT_ARROW_STRINGS, null);
		log.debug("Split Relation " + value + " into following parts: " + getValueNotNull(leftArrow) + " | " + getValueNotNull(lineType) + " | " + getValueNotNull(rightArrow));

		drawLineAndArrows(drawer, relationPoints, lineType, leftArrow, rightArrow);
		state.setFacetResponse(RelationLineTypeFacet.class, true); // let Relation know that lt= is set
	}

	public static void drawDefaultLineAndArrows(DrawHandler drawer, RelationPoints relationPoints) {
		drawLineAndArrows(drawer, relationPoints, new Match<LineType>("", LineType.SOLID), new Match<ArrowEnd>("", null), new Match<ArrowEnd>("", null));
	}

	private static void drawLineAndArrows(DrawHandler drawer, RelationPoints relationPoints, Match<LineType> lineType, Match<ArrowEnd> leftArrow, Match<ArrowEnd> rightArrow) {
		drawLineBetweenPoints(drawer, relationPoints, lineType.type);
		drawArrowEnds(drawer, relationPoints, leftArrow, rightArrow);
		relationPoints.resizeRectAndReposPoints(); // line description and relation-endings can change the relation size, therefore recalc it now
	}

	private static void drawArrowEnds(DrawHandler drawer, RelationPoints relationPoints, Match<ArrowEnd> leftArrow, Match<ArrowEnd> rightArrow) {
		ColorOwn oldBgColor = drawer.getStyle().getBackgroundColor();
		drawer.setBackgroundColor(oldBgColor.transparency(Transparency.FOREGROUND)); // arrow background is not transparent
		print(drawer, relationPoints, leftArrow, relationPoints.getFirstLine(), true);
		print(drawer, relationPoints, rightArrow, relationPoints.getLastLine(), false);
		drawer.setBackgroundColor(oldBgColor); // reset background
	}

	private static void print(DrawHandler drawer, ResizableObject relationPoints, Match<ArrowEnd> match, Line line, boolean drawOnLineStart) {
		relationPoints.resetPointMinSize(((PointDoubleIndexed) line.getPoint(drawOnLineStart)).getIndex());
		if (match.type != null) {
			match.type.print(drawer, line, drawOnLineStart, match.text, relationPoints);
		}
	}

	private static void drawLineBetweenPoints(DrawHandler drawer, RelationPoints relationPoints, LineType lineType) {
		LineType oldLt = drawer.getStyle().getLineType();
		drawer.setLineType(lineType);
		relationPoints.drawLinesBetweenPoints(drawer);
		drawer.setLineType(oldLt);
	}

	private <T extends RegexValueHolder> Match<T> extractPart(List<T> valueHolderList, T defaultValue) {
		for (T valueHolder : valueHolderList) {
			String regex = "^" + valueHolder.getRegexValue(); // only match from start of the line (left to right)
			String newRemainingValue = remainingValue.replaceFirst(regex, "");
			if (!remainingValue.equals(newRemainingValue)) {
				String removedPart = remainingValue.substring(0, remainingValue.length() - newRemainingValue.length());
				remainingValue = newRemainingValue;
				return new Match<T>(removedPart, valueHolder);
			}
		}
		return new Match<T>("", defaultValue);
	}

	private String getValueNotNull(Match<? extends RegexValueHolder> valueHolder) {
		if (valueHolder.type == null) {
			return "";
		}
		else {
			return valueHolder.type.getRegexValue();
		}
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(
				"lt",
				new ValueInfo("->", "relation with arrow to the right", "R0lGODlhPgALANUAAAAAAP////v7+/f39+/v7+vr6+fn59/f39vb29fX19PT08vLy8PDw7e3t6urq6Ojo5ubm5eXl4eHh4ODg4CAgHh4eGxsbGhoaGBgYFhYWFRUVEhISEBAQDg4ODQ0NCQkJCAgIBwcHBgYGAwMDP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACQALAAAAAA+AAsAAAZZwIBwSCwaj8ikcaFBKJ/QaFQQCVUI0qxWSrCEINuw2JjYeBjjtLjxyRwCFIB8Tq/b7/i8nj56q/9PDyAXBYCGRwsdHAqHjUMGGB8Ojo4DEyISA5SOChgGSEEAOw=="),
				new ValueInfo(
						"<..>>",
						"open left, closed right arrow and dotted line",
						"R0lGODlhPwANAPcAAAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra2xsbG1tbW5ubm9vb3BwcHFxcXJycnNzc3R0dHV1dXZ2dnd3d3h4eHl5eXp6ent7e3x8fH19fX5+fn9/f4CAgIGBgYKCgoODg4SEhIWFhYaGhoeHh4iIiImJiYqKiouLi4yMjI2NjY6Ojo+Pj5CQkJGRkZKSkpOTk5SUlJWVlZaWlpeXl5iYmJmZmZqampubm5ycnJ2dnZ6enp+fn6CgoKGhoaKioqOjo6SkpKWlpaampqenp6ioqKmpqaqqqqurq6ysrK2tra6urq+vr7CwsLGxsbKysrOzs7S0tLW1tba2tre3t7i4uLm5ubq6uru7u7y8vL29vb6+vr+/v8DAwMHBwcLCwsPDw8TExMXFxcbGxsfHx8jIyMnJycrKysvLy8zMzM3Nzc7Ozs/Pz9DQ0NHR0dLS0tPT09TU1NXV1dbW1tfX19jY2NnZ2dra2tvb29zc3N3d3d7e3t/f3+Dg4OHh4eLi4uPj4+Tk5OXl5ebm5ufn5+jo6Onp6erq6uvr6+zs7O3t7e7u7u/v7/Dw8PHx8fLy8vPz8/T09PX19fb29vf39/j4+Pn5+fr6+vv7+/z8/P39/f7+/v///ywAAAAAPwANAAAIwgD/CRxIsKDBgwgTHlynEOE5MNMaSpxYEAyWcRTvHcIw6B7FjxLBIMEAyKPCVSTAnAPJUiGYT+vIkDiFcBoQHMta6kT4UuAyHjyeEVyHBsSonUgN9hx4CgQZht8YANDzDxAAQFWvZsVqlavWrlvDgh379SoYKGX1sMDw7d83LCRuJZ0rcKnAWyiwtB04jAaSa3SRLt3GBMYwhJs4sHkXuOXLd3gwbNqnEDKHS5QbUzzLAQ7jiduo5NQ8cRUTwKRTTwwIADs="));
	}
}
