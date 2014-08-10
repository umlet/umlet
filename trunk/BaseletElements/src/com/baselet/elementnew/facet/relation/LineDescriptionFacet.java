package com.baselet.elementnew.facet.relation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.element.uml.relation.RelationPoints;
import com.baselet.elementnew.element.uml.relation.SettingsRelation;
import com.baselet.elementnew.facet.GlobalFacet;
import com.baselet.gui.AutocompletionText;

public class LineDescriptionFacet extends GlobalFacet {

	public static LineDescriptionFacet INSTANCE = new LineDescriptionFacet();

	public static class LineDescriptionFacetResponse {
		private int middleLines = 0;
		private final Set<Integer> alreadysetIndexes = new HashSet<Integer>();

		public Set<Integer> getAlreadysetIndexes() {
			return alreadysetIndexes;
		}

		public int getAndIncreaseMiddleLines() {
			return middleLines++;
		}
	}

	private static final int DISTANCE_TO_LINE = 3;

	private LineDescriptionFacet() {}

	private static final int MESSAGE_START_INDEX = 1;
	private static final int MESSAGE_END_INDEX = 2;
	private static final int MESSAGE_MIDDLE_FIRST_INDEX = 3;

	private static final String MESSAGE_CHAR = "m";
	static final String MESSAGE_START_KEY = MESSAGE_CHAR + MESSAGE_START_INDEX;
	static final String MESSAGE_END_KEY = MESSAGE_CHAR + MESSAGE_END_INDEX;

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return true; // apply alway because middle text has no prefix
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(
				new AutocompletionText(MESSAGE_START_KEY + SEP, "message at start"),
				new AutocompletionText(MESSAGE_END_KEY + SEP, "message at end")
				);
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		Map<String, Integer> displacements = state.getOrInitFacetResponse(DescriptionPositionFacet.class, new HashMap<String, Integer>());
		RelationPoints relationPoints = ((SettingsRelation) state.getSettings()).getRelationPoints();
		LineDescriptionFacetResponse response = state.getOrInitFacetResponse(LineDescriptionFacet.class, new LineDescriptionFacetResponse());

		PointDouble pointText = null;
		if (line.startsWith(MESSAGE_START_KEY + SEP) || line.startsWith(MESSAGE_END_KEY + SEP)) {
			String[] split = line.split(SEP, -1);
			String key = split[0];
			String text = split[1];
			if (!text.isEmpty()) {
				if (key.equals(MESSAGE_START_KEY)) {
					pointText = calcPosOfEndText(drawer, text, relationPoints.getFirstLine(), true);
					printAndUpdateIndex(drawer, response, relationPoints, pointText, MESSAGE_START_INDEX, text, displacements);
				}
				else if (key.equals(MESSAGE_END_KEY)) {
					pointText = calcPosOfEndText(drawer, text, relationPoints.getLastLine(), false);
					printAndUpdateIndex(drawer, response, relationPoints, pointText, MESSAGE_END_INDEX, text, displacements);
				}
			}
		}
		else /* middle text has no prefix */{
			String text = replaceArrowsWithUtf8Characters(line);
			pointText = calcPosOfMiddleText(relationPoints.getDragBox().getCenter(), drawer.textWidth(text));
			int number = response.getAndIncreaseMiddleLines();
			pointText = new PointDouble(pointText.getX(), pointText.getY() + number * drawer.textHeightWithSpace());
			printAndUpdateIndex(drawer, response, relationPoints, pointText, MESSAGE_MIDDLE_FIRST_INDEX + number, text);
		}
	}

	private void printAndUpdateIndex(DrawHandler drawer, LineDescriptionFacetResponse response, RelationPoints relationPoints, PointDouble pointText, int index, String text, Map<String, Integer> displacements) {
		double xDisp = pointText.getX() + getOrZero(index, displacements, DescriptionPositionFacet.X_KEY);
		double yDisp = pointText.getY() + getOrZero(index, displacements, DescriptionPositionFacet.Y_KEY);
		printAndUpdateIndex(drawer, response, relationPoints, new PointDouble(xDisp, yDisp), index, text);
	}

	private Integer getOrZero(int index, Map<String, Integer> displacements, String key) {
		Integer disp = displacements.get(MESSAGE_CHAR + index + key);
		if (disp == null) {
			disp = 0;
		}
		return disp;
	}

	private void printAndUpdateIndex(DrawHandler drawer, LineDescriptionFacetResponse response, RelationPoints relationPoints, PointDouble pointText, int index, String text) {
		drawer.print(text, pointText, AlignHorizontal.LEFT);

		// to make sure text is printed (and therefore withing relation-element-borders, resize relation according to text
		relationPoints.setTextBox(index, new Rectangle(pointText.getX(), pointText.getY() - drawer.textHeight(), drawer.textWidth(text), drawer.textHeight()));
		// and remember that this index is set
		response.getAlreadysetIndexes().add(index);
		relationPoints.resizeRectAndReposPoints(); // apply the (possible) changes now to make sure the following facets use correct coordinates
	}

	private static String replaceArrowsWithUtf8Characters(String text) {
		if (text.startsWith("< ")) {
			text = "\u25C4" + text.substring(1);
		}
		if (text.endsWith(" >")) {
			text = text.substring(0, text.length() - 1) + "\u25BA";
		}
		else if (text.endsWith(" ^")) {
			text = text.substring(0, text.length() - 1) + "\u25B2";
		}
		else if (text.endsWith(" v")) {
			text = text.substring(0, text.length() - 1) + "\u25BC";
		}
		return text;
	}

	private PointDouble calcPosOfEndText(DrawHandler drawer, String text, Line line, boolean isStart) {
		PointDouble pointText = line.getPointOnLineWithDistanceFrom(isStart, 15); // distance from lineend (because of arrows,...)
		Direction lineDirection = line.getDirectionOfLine(isStart);
		if (lineDirection == Direction.RIGHT) {
			pointText = new PointDouble(pointText.getX() - drawer.textWidth(text), pointText.getY() + drawer.textHeight());
		}
		else if (lineDirection == Direction.LEFT) {
			pointText = new PointDouble(pointText.getX(), pointText.getY() + drawer.textHeight());
		}
		else if (lineDirection == Direction.UP) {
			pointText = new PointDouble(pointText.getX() + 5, pointText.getY() + drawer.textHeight() * 0.7);
		}
		else if (lineDirection == Direction.DOWN) {
			pointText = new PointDouble(pointText.getX() + 5, pointText.getY());
		}
		return pointText;
	}

	private PointDouble calcPosOfMiddleText(PointDouble center, double textWidth) {
		double textX = center.getX() - textWidth / 2;
		double textY = center.getY() - DISTANCE_TO_LINE;
		return new PointDouble(textX, textY);
	}

	@Override
	public Priority getPriority() {
		return Priority.LOWEST; // because the middle text has no prefix, it should only apply after every other facet. also text DescriptionPositionFacet must be known before calculating the text position
	}
}
