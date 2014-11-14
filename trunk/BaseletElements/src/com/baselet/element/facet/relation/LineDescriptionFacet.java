package com.baselet.element.facet.relation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.geom.Line;
import com.baselet.control.geom.Point;
import com.baselet.control.geom.PointDouble;
import com.baselet.control.geom.Rectangle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.elementnew.uml.relation.RelationPointHandler;
import com.baselet.element.elementnew.uml.relation.SettingsRelation;
import com.baselet.element.facet.GlobalFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class LineDescriptionFacet extends GlobalFacet {

	private static final int X_DIST_TO_LINE = 4;
	private static final int LOWER_Y_DIST_TO_LINE = 1;
	private static final int UPPER_Y_DIST_TO_LINE = 5;
	private static final int MIDDLE_DISTANCE_TO_LINE = 4;

	public static final LineDescriptionFacet INSTANCE = new LineDescriptionFacet();

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

	private LineDescriptionFacet() {}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return true; // apply alway because middle text has no prefix
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(
				new AutocompletionText(LineDescriptionEnum.MESSAGE_START.getKey() + SEP, "message at start"),
				new AutocompletionText(LineDescriptionEnum.MESSAGE_END.getKey() + SEP, "message at end"),
				new AutocompletionText(LineDescriptionEnum.ROLE_START.getKey() + SEP, "role at start"),
				new AutocompletionText(LineDescriptionEnum.ROLE_END.getKey() + SEP, "role at end")
				);
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		Map<String, Point> displacements = state.getOrInitFacetResponse(DescriptionPositionFacet.class, new HashMap<String, Point>());
		RelationPointHandler relationPoints = ((SettingsRelation) state.getSettings()).getRelationPoints();
		LineDescriptionFacetResponse response = state.getOrInitFacetResponse(LineDescriptionFacet.class, new LineDescriptionFacetResponse());

		PointDouble pointText = null;
		LineDescriptionEnum enumVal = LineDescriptionEnum.forString(line);
		if (enumVal == LineDescriptionEnum.MESSAGE_MIDDLE) {
			String text = replaceArrowsWithUtf8Characters(line);
			pointText = calcPosOfMiddleText(relationPoints.getDragBox().getCenter(), drawer, text, relationPoints.getFirstLine());
			int number = response.getAndIncreaseMiddleLines();
			pointText = new PointDouble(pointText.getX(), pointText.getY() + number * drawer.textHeightMaxWithSpace());
			printAndUpdateIndex(drawer, response, relationPoints, pointText, enumVal.getIndex() + number, text);
		}
		else {
			String[] split = line.split(SEP, -1);
			String text = split[1];
			if (!text.isEmpty()) {
				pointText = calcPosOfEndText(drawer, text, relationPoints.getFirstLine(), enumVal);
				printAndUpdateIndex(drawer, response, relationPoints, pointText, enumVal.getIndex(), text, displacements.get(enumVal.getKey()));

			}
		}
	}

	private void printAndUpdateIndex(DrawHandler drawer, LineDescriptionFacetResponse response, RelationPointHandler relationPoints, PointDouble pointText, int index, String text, Point displacement) {
		if (displacement != null) {
			pointText = new PointDouble(pointText.getX() + displacement.getX(), pointText.getY() + displacement.getY());
		}
		printAndUpdateIndex(drawer, response, relationPoints, pointText, index, text);
	}

	private void printAndUpdateIndex(DrawHandler drawer, LineDescriptionFacetResponse response, RelationPointHandler relationPoints, PointDouble pointText, int index, String text) {
		drawer.print(text, pointText, AlignHorizontal.LEFT);

		// to make sure text is printed (and therefore withing relation-element-borders, resize relation according to text
		relationPoints.setTextBox(index, new Rectangle(pointText.getX(), pointText.getY() - drawer.textHeightMax(), drawer.textWidth(text), drawer.textHeightMax()));
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

	// PROBLEM: ich muss wissen ob es r1/r2 gibt damit ich die position von m1/m2 bestimmen kann -> separate facets mit anderen priorit#ten??
	private PointDouble calcPosOfEndText(DrawHandler drawer, String text, Line line, LineDescriptionEnum enumVal) {
		Boolean printOnStart = enumVal.isStart();
		PointDouble pointText = line.getPointOnLineWithDistanceFrom(printOnStart, 15); // distance from lineend (because of arrows,...)
		Direction lineDirection = line.getDirectionOfLine(printOnStart);

		// Default Positioning
		if (lineDirection == Direction.RIGHT) {
			pointText = new PointDouble(pointText.getX() - drawer.textWidth(text) - drawer.getDistanceBorderToText(), pointText.getY() + drawer.textHeightMax() + LOWER_Y_DIST_TO_LINE);
		}
		else if (lineDirection == Direction.LEFT) {
			pointText = new PointDouble(pointText.getX() + X_DIST_TO_LINE, pointText.getY() + drawer.textHeightMax() + LOWER_Y_DIST_TO_LINE);
		}
		else if (lineDirection == Direction.UP) {
			pointText = new PointDouble(pointText.getX() + X_DIST_TO_LINE, pointText.getY() + drawer.textHeightMax() + LOWER_Y_DIST_TO_LINE);
		}
		else if (lineDirection == Direction.DOWN) {
			pointText = new PointDouble(pointText.getX() + X_DIST_TO_LINE, pointText.getY() - LOWER_Y_DIST_TO_LINE);
		}

		// r1 and r2 are place on top of the line if it is horizontal or on the inner side if the line is vertical
		if (enumVal.isRoleStartOrEnd()) {
			if (lineDirection == Direction.UP) {
				pointText = new PointDouble(pointText.getX(), pointText.getY() + drawer.textHeightMaxWithSpace());
			}
			else if (lineDirection == Direction.DOWN) {
				pointText = new PointDouble(pointText.getX(), pointText.getY() - drawer.textHeightMaxWithSpace());
			}
			else {
				pointText = new PointDouble(pointText.getX(), pointText.getY() - drawer.textHeightMax() - UPPER_Y_DIST_TO_LINE);
			}
		}
		return pointText;
	}

	private PointDouble calcPosOfMiddleText(PointDouble center, DrawHandler drawer, String text, Line line) {
		double textWidth = drawer.textWidth(text);
		boolean horizontalLine = line.getDirectionOfLine(true).isHorizontal();

		double textX, textY;
		if (horizontalLine) {
			textX = center.getX() - textWidth / 2;
			textY = center.getY() - MIDDLE_DISTANCE_TO_LINE;
		}
		else {
			textX = center.getX() + X_DIST_TO_LINE;
			textY = center.getY() + drawer.textHeight(text) / 2;
		}
		return new PointDouble(textX, textY);
	}

	@Override
	public Priority getPriority() {
		return Priority.LOWEST; // because the middle text has no prefix, it should only apply after every other facet. also text DescriptionPositionFacet must be known before calculating the text position
	}
}
