package com.baselet.element.relation.facet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Priority;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.relation.helper.LineDescriptionEnum;
import com.baselet.element.relation.helper.RelationPointHandler;
import com.baselet.gui.AutocompletionText;

public class LineDescriptionFacet extends Facet {

	static final int X_DIST_TO_LINE = 4;
	static final int LOWER_Y_DIST_TO_LINE = 1;
	static final int UPPER_Y_DIST_TO_LINE = 5;
	static final int MIDDLE_DISTANCE_TO_LINE = 4;

	public static final LineDescriptionFacet INSTANCE = new LineDescriptionFacet();

	private LineDescriptionFacet() {}

	public static class LineDescriptionFacetResponse {
		private final List<String> middleLines = new ArrayList<String>();
		private final Set<Integer> alreadysetIndexes = new HashSet<Integer>();

		public Set<Integer> getAlreadysetIndexes() {
			return alreadysetIndexes;
		}

		public void addMiddleLine(String line) {
			middleLines.add(line);
		}

	}

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
	public void handleLine(String line, PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		Map<String, Point> displacements = state.getOrInitFacetResponse(LineDescriptionPositionFacet.class, new HashMap<String, Point>());
		RelationPointHandler relationPoints = getRelationPoints(state);
		LineDescriptionFacetResponse response = getOrInitOwnResponse(state);

		LineDescriptionEnum enumVal = LineDescriptionEnum.forString(line);
		if (enumVal == LineDescriptionEnum.MESSAGE_MIDDLE) {
			response.addMiddleLine(LineDescriptionUtils.replaceArrowsWithUtf8Characters(line)); // middle line can only be printed after all lines are collected
		}
		else {
			String[] split = line.split(SEP, -1);
			String text = split[1];
			if (!text.isEmpty()) {
				PointDouble pointText = LineDescriptionUtils.calcPosOfEndText(drawer, text, relationPoints, enumVal);
				printAndUpdateIndex(drawer, response, relationPoints, pointText, enumVal.getIndex(), text, displacements.get(enumVal.getKey()));

			}
		}
	}

	private LineDescriptionFacetResponse getOrInitOwnResponse(PropertiesParserState state) {
		return state.getOrInitFacetResponse(LineDescriptionFacet.class, new LineDescriptionFacetResponse());
	}

	private RelationPointHandler getRelationPoints(PropertiesParserState state) {
		return ((SettingsRelation) state.getSettings()).getRelationPoints();
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

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		LineDescriptionFacetResponse response = getOrInitOwnResponse(state);

		if (!handledLines.isEmpty()) {
			DrawHandler drawer = state.getDrawer();
			int i = 0;
			for (String text : response.middleLines) {
				RelationPointHandler relationPoints = getRelationPoints(state);
				PointDouble pointText = LineDescriptionUtils.calcPosOfMiddleText(drawer, text, relationPoints.getMiddleLine(), response.middleLines.size());
				int number = i++;
				pointText = new PointDouble(pointText.getX(), pointText.getY() + number * drawer.textHeightMaxWithSpace());
				printAndUpdateIndex(drawer, response, relationPoints, pointText, LineDescriptionEnum.MESSAGE_MIDDLE.getIndex() + number, text);
			}
		}

		// all unused textboxes must be reset to default size (to make sure the relation size is correct even if LineDescriptionFacet is never called)
		getRelationPoints(state).resetTextBoxIndexesExcept(response.getAlreadysetIndexes());

	}

	@Override
	public Priority getPriority() {
		return Priority.LOWEST; // because the middle text has no prefix, it should only apply after every other facet. also text DescriptionPositionFacet must be known before calculating the text position
	}
}
