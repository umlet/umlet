package com.baselet.elementnew.facet.relation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baselet.control.SharedConstants;
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

	private static final int DISTANCE_TO_LINE = 4;

	public static LineDescriptionFacet INSTANCE = new LineDescriptionFacet();

	private LineDescriptionFacet() {}

	private static final String MESSAGE_START_KEY = "m1";
	private static final String MESSAGE_END_KEY = "m2";
	private static final String MESSAGE_MIDDLE_KEY = "mm";

	private static final Map<String, Integer> indexMap = new HashMap<String, Integer>();
	static {
		indexMap.put(MESSAGE_START_KEY, 0);
		indexMap.put(MESSAGE_END_KEY, 1);
		indexMap.put(MESSAGE_MIDDLE_KEY, 2);
	}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return line.startsWith(MESSAGE_START_KEY + SEP) || line.startsWith(MESSAGE_MIDDLE_KEY + SEP) || line.startsWith(MESSAGE_END_KEY + SEP);
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(
				new AutocompletionText(MESSAGE_START_KEY + SEP, "message at start"),
				new AutocompletionText(MESSAGE_END_KEY + SEP, "message at end"),
				new AutocompletionText(MESSAGE_MIDDLE_KEY + SEP, "message in the middle")
				);
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		RelationPoints relationPoints = ((SettingsRelation) state.getSettings()).getRelationPoints();
		String[] split = line.split(SEP, -1);
		String key = split[0];
		String text = split[1];
		PointDouble pointText = null;
		if (!text.isEmpty()) {
			if (key.equals(MESSAGE_START_KEY)) {
				pointText = calcPos(drawer, text, pointText, relationPoints.getFirstLine(), true);
			}
			else if (key.equals(MESSAGE_END_KEY)) {
				pointText = calcPos(drawer, text, pointText, relationPoints.getLastLine(), false);
			}
			else /* if (key.equals(MESSAGE_MIDDLE_KEY)) */{
				// replace < and > with UTF-8 triangles but avoid replacing quotations which are already replaced later
				if (text.replaceAll(SharedConstants.LEFT_QUOTATION, "").startsWith("<")) {
					text = "\u25C4 " + text.substring(1);
				}
				if (text.replaceAll(SharedConstants.RIGHT_QUOTATION, "").endsWith(">")) {
					text = text.substring(0, text.length() - 1) + " \u25BA";
				}
				pointText = calcPosOfMiddleText(relationPoints.getDragBox().getCenter(), drawer.textWidth(text));
			}
			drawer.print(text, pointText, AlignHorizontal.LEFT);

			// to make sure text is printed (and therefore withing relation-element-borders, resize relation according to text
			int index = indexMap.get(key);
			if (index != -1) {
				relationPoints.setTextBox(index, new Rectangle(pointText.getX(), pointText.getY() - drawer.textHeight(), drawer.textWidth(text), drawer.textHeight()));
				// and remember that this index is set
				Set<Integer> alreadysetIndexes = state.getFacetResponse(LineDescriptionFacet.class, new HashSet<Integer>());
				state.setFacetResponse(LineDescriptionFacet.class, alreadysetIndexes);
				alreadysetIndexes.add(index);
			}
		}
	}

	private PointDouble calcPos(DrawHandler drawer, String text, PointDouble pointText, Line line, boolean isStart) {
		pointText = line.getPointOnLineWithDistanceFrom(isStart, 20);
		pointText = littleDistanceIfAngleZero(pointText, line);
		Direction lineDirection = line.getDirectionOfLine(isStart);
		if (lineDirection == Direction.RIGHT) {
			pointText = new PointDouble(pointText.getX() - drawer.textWidth(text), pointText.getY());
		}
		else if (lineDirection == Direction.UP) {
			pointText = new PointDouble(pointText.getX() + DISTANCE_TO_LINE, pointText.getY() + drawer.textHeight() * 0.7);
		}
		else if (lineDirection == Direction.DOWN) {
			pointText = new PointDouble(pointText.getX() + DISTANCE_TO_LINE, pointText.getY());
		}
		return pointText;
	}

	private PointDouble littleDistanceIfAngleZero(PointDouble pointText, Line line) {
		double angleOfSlope = line.getAngleOfSlope();
		if (angleOfSlope == 0.0 || angleOfSlope == 180.0) {
			return new PointDouble(pointText.getX(), pointText.getY() - DISTANCE_TO_LINE);
		}
		else {
			return pointText;
		}
	}

	public static Collection<Integer> getAllIndexes() {
		return indexMap.values();
	}

	private PointDouble calcPosOfMiddleText(PointDouble center, double textWidth) {
		double textX = center.getX() - textWidth / 2;
		double textY = center.getY() - DISTANCE_TO_LINE;
		return new PointDouble(textX, textY);
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGH; // should match before RelationLineTypeFacet to make sure resizings due to textbox-size happen before
	}
}
