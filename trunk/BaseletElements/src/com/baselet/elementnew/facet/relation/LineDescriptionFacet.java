package com.baselet.elementnew.facet.relation;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.SharedConstants;
import com.baselet.control.enumerations.AlignHorizontal;
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
	private static final int DISTANCE_TO_ARROW = 4;

	public static LineDescriptionFacet INSTANCE = new LineDescriptionFacet();

	private LineDescriptionFacet() {}

	private static final String MESSAGE_START_KEY = "m1";
	private static final String MESSAGE_END_KEY = "m2";
	private static final String MESSAGE_MIDDLE_KEY = "mm";

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
				Line arrowLine = relationPoints.getFirstLine();
				double yDisp = calcYDisplacement(arrowLine.getStart().getY(), arrowLine.getEnd().getY(), drawer);
				pointText = calcPosOfArrowText(arrowLine.getStart(), drawer.textWidth(text), drawer.textHeight(), yDisp);
			}
			else if (key.equals(MESSAGE_END_KEY)) {
				Line arrowLine = relationPoints.getLastLine();
				double yDisp = calcYDisplacement(arrowLine.getEnd().getY(), arrowLine.getStart().getY(), drawer);
				pointText = calcPosOfArrowText(relationPoints.getLastLine().getEnd(), drawer.textWidth(text), drawer.textHeight(), yDisp);
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
			int index = key.equals(MESSAGE_START_KEY) ? 0 : key.equals(MESSAGE_MIDDLE_KEY) ? 1 : key.equals(MESSAGE_END_KEY) ? 2 : -1;
			if (index != -1) {
				relationPoints.setTextBox(index, new Rectangle(pointText.getX(), pointText.getY() - drawer.textHeight(), drawer.textWidth(text), drawer.textHeight()));
			}
		}
	}

	private double calcYDisplacement(double y1, double y2, DrawHandler drawer) {
		if (y1 == y2) {
			return DISTANCE_TO_LINE;
		}
		else if (y1 > y2) {
			return drawer.textHeight();
		}
		else {
			return -drawer.textHeight() - 10;
		}
	}

	private PointDouble calcPosOfArrowText(PointDouble pointArrow, double textWidth, double textHeight, double yDisplacement) {
		double textX = pointArrow.getX() + RelationPoints.POINT_SELECTION_RADIUS + DISTANCE_TO_ARROW; // default x-pos is at the right end of selectionradius
		double textY = pointArrow.getY() - yDisplacement;

		// if text would be placed on the right outside of the relation and there is enough space to place it inside, do so
		double selectionDiameter = RelationPoints.POINT_SELECTION_RADIUS * 2;
		double textXWithinRelationSpace = textX - textWidth - selectionDiameter - DISTANCE_TO_ARROW * 2;
		if (textXWithinRelationSpace > selectionDiameter) {
			textX = textXWithinRelationSpace;
		}

		// if text wouldn't fit on top of the relation (normally for arrow points which are on the upper most position of the relation), place it on bottom of it
		if (textY < textHeight) {
			textY += textHeight; // make sure larger fontsizes will still fit
		}
		return new PointDouble(textX, textY);
	}

	private PointDouble calcPosOfMiddleText(PointDouble center, double textWidth) {
		double textX = center.getX() - textWidth / 2;
		double textY = center.getY() - DISTANCE_TO_LINE;

		// if text would not be visible at the left relation part, move it to visible area
		if (textX < 0) {
			textX += textWidth / 2 + RelationPoints.DRAG_BOX_SIZE;
		}

		return new PointDouble(textX, textY);
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGH; // should match before RelationLineTypeFacet to make sure resizings due to textbox-size happen before
	}
}
