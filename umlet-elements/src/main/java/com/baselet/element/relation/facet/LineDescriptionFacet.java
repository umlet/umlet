package com.baselet.element.relation.facet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baselet.control.SharedUtils;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Priority;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.FirstRunFacet;
import com.baselet.element.facet.KeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.relation.helper.LineDescriptionEnum;
import com.baselet.element.relation.helper.RelationPointHandler;
import com.baselet.gui.AutocompletionText;

/**
 * must be in first-run after LineDescriptionPositionFacet (because the displacement must be applied)
 * and before RelationLineTypeFacet and before drawCommonComponents (because the text changes the relationpoint placements and the size of the relation
 */
public class LineDescriptionFacet extends FirstRunFacet {

	static final int X_DIST_TO_LINE = 4;
	static final int LOWER_Y_DIST_TO_LINE = 1;
	static final int UPPER_Y_DIST_TO_LINE = 5;
	static final int MIDDLE_DISTANCE_TO_LINE = 4;

	public static final LineDescriptionFacet INSTANCE = new LineDescriptionFacet();

	private LineDescriptionFacet() {}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return !line.startsWith(RelationLineTypeFacet.KEY + KeyValueFacet.SEP); // because middle text has no prefix, apply alway except for the lt= line
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(
				new AutocompletionText(LineDescriptionEnum.MESSAGE_START.getKey() + KeyValueFacet.SEP, "message at start"),
				new AutocompletionText(LineDescriptionEnum.MESSAGE_END.getKey() + KeyValueFacet.SEP, "message at end"),
				new AutocompletionText(LineDescriptionEnum.ROLE_START.getKey() + KeyValueFacet.SEP, "role at start"),
				new AutocompletionText(LineDescriptionEnum.ROLE_END.getKey() + KeyValueFacet.SEP, "role at end"));
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		// only act on parsingFinished() when all lines are known and other first-run-facets have been resolved (e.g. fg-color)
	}

	private RelationPointHandler getRelationPoints(PropertiesParserState state) {
		return ((SettingsRelation) state.getSettings()).getRelationPoints();
	}

	private void addIndex(RelationPointHandler relationPoints, int index, Set<Integer> usedIndexes, Rectangle rect) {
		relationPoints.setTextBox(index, rect);
		usedIndexes.add(index);
	}

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		Map<String, Point> displacements = state.getOrInitFacetResponse(LineDescriptionPositionFacet.class, new HashMap<String, Point>());
		RelationPointHandler relationPoints = getRelationPoints(state);
		DrawHandler drawer = state.getDrawer();
		Set<Integer> usedIndexes = new HashSet<Integer>();

		List<String> middleLines = new ArrayList<String>();
		List<String> otherLines = new ArrayList<String>();
		for (String line : handledLines) {
			if (LineDescriptionEnum.forString(line) == LineDescriptionEnum.MESSAGE_MIDDLE) {
				middleLines.add(line);
			}
			else {
				otherLines.add(line);
			}
		}
		printMiddleDescription(relationPoints, drawer, usedIndexes, middleLines);
		printEndDescriptions(displacements, relationPoints, drawer, usedIndexes, otherLines);

		// all unused textboxes must be reset to default size (to make sure the relation size is correct even if LineDescriptionFacet is never called)
		relationPoints.resetTextBoxIndexesExcept(usedIndexes);
		relationPoints.resizeRectAndReposPoints(); // apply the (possible) changes now to make sure the following facets use correct coordinates
	}

	private void printMiddleDescription(RelationPointHandler relationPoints, DrawHandler drawer, Set<Integer> usedIndexes, List<String> middleLines) {
		double halfMiddleBlockHeight = middleLines.size() * drawer.textHeightMaxWithSpace() / 2; // because vertical text blocks should be centered, the half of the total text block must be subtracted
		Rectangle textSpace = null;
		for (int i = 0; i < middleLines.size(); i++) {
			String line = LineDescriptionUtils.replaceArrowsWithUtf8Characters(middleLines.get(i));
			PointDouble pointText = LineDescriptionUtils.calcPosOfMiddleText(drawer, line, relationPoints.getMiddleLine(), i, halfMiddleBlockHeight);
			drawer.print(line, pointText, AlignHorizontal.LEFT);
			textSpace = increaseTextSpaceRectangleForLine(textSpace, drawer, line, pointText);
		}
		if (textSpace != null) {
			addIndex(relationPoints, LineDescriptionEnum.MESSAGE_MIDDLE.getIndex(), usedIndexes, textSpace);
		}
	}

	private void printEndDescriptions(Map<String, Point> displacements, RelationPointHandler relationPoints, DrawHandler drawer, Set<Integer> usedIndexes, List<String> otherLines) {
		for (String line : otherLines) {
			LineDescriptionEnum enumVal = LineDescriptionEnum.forString(line);
			String text = line.substring(line.indexOf(KeyValueFacet.SEP) + 1);
			if (!text.isEmpty()) {
				Rectangle textSpace = null;
				String[] splitAtLineEndChar = SharedUtils.splitAtLineEndChar(text);
				for (int i = 0; i < splitAtLineEndChar.length; i++) { // end description text can be split up with \n into multiple lines
					String subline = splitAtLineEndChar[i];
					PointDouble pointText = LineDescriptionUtils.calcPosOfLineDescriptionText(drawer, subline, i, splitAtLineEndChar.length, relationPoints, enumVal);
					pointText = applyDisplacements(displacements, enumVal, pointText);
					drawer.print(subline, pointText, AlignHorizontal.LEFT);
					textSpace = increaseTextSpaceRectangleForLine(textSpace, drawer, subline, pointText);
				}
				addIndex(relationPoints, enumVal.getIndex(), usedIndexes, textSpace);
			}
		}
	}

	private PointDouble applyDisplacements(Map<String, Point> displacements, LineDescriptionEnum enumVal, PointDouble pointText) {
		Point displacement = displacements.get(enumVal.getKey());
		if (displacement != null) {
			pointText = new PointDouble(pointText.getX() + displacement.getX(), pointText.getY() + displacement.getY());
		}
		return pointText;
	}

	private Rectangle increaseTextSpaceRectangleForLine(Rectangle textSpaceRect, DrawHandler drawer, String line, PointDouble pointText) {
		Rectangle newSpaceRect = new Rectangle(pointText.getX(), pointText.getY() - drawer.textHeightMax(), drawer.textWidth(line), drawer.textHeightMax());
		textSpaceRect = Rectangle.mergeToLeft(textSpaceRect, newSpaceRect);
		return textSpaceRect;
	}

	@Override
	public Priority getPriority() {
		return Priority.LOWEST; // because the middle text has no prefix, it should only apply after every other facet. also text DescriptionPositionFacet must be known before calculating the text position
	}
}
