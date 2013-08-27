package com.umlet.element.experimental.element.uml.relation.facet;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.element.uml.relation.RelationPoints;
import com.umlet.element.experimental.element.uml.relation.SettingsRelation;
import com.umlet.element.experimental.facets.AbstractGlobalFacet;

public class LineDescriptionFacet extends AbstractGlobalFacet {
	
	public static LineDescriptionFacet INSTANCE = new LineDescriptionFacet();
	private LineDescriptionFacet() {}

	private static final String MESSAGE_START_KEY = "m1";
	private static final String MESSAGE_END_KEY = "m2";
	private static final String MESSAGE_MIDDLE_KEY = "mm";
	@Override
	public boolean checkStart(String line) {
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
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		RelationPoints relationPoints = ((SettingsRelation) propConfig.getSettings()).getRelationPoints();
		String[] split = line.split(SEP, -1);
		String key = split[0];
		String text = split[1];
		double textWidth = drawer.textWidth(text);
		double textHeight = drawer.textHeight();
		PointDouble pointText = null;
		if (!text.isEmpty()) {
			if (key.equals(MESSAGE_START_KEY)) {
				pointText = calcPosOfArrowText(relationPoints.getFirstLine().getStart(), textWidth, textHeight);
			} else if (key.equals(MESSAGE_END_KEY)) {
				pointText = calcPosOfArrowText(relationPoints.getLastLine().getEnd(), textWidth, textHeight);
			} else /*if (key.equals(MESSAGE_MIDDLE_KEY))*/ {
				pointText = calcPosOfMiddleText(relationPoints.getDragBox().getCenter(), textWidth, textHeight);
			}
			drawer.print(text, pointText, AlignHorizontal.LEFT);
			
			// to make sure text is printed (and therefore withing relation-element-borders, resize relation according to text
			relationPoints.resizeRelationSpaceToMakeTextVisible(textWidth+pointText.getX(), pointText.getY());
		}
	}

	private PointDouble calcPosOfArrowText(PointDouble pointArrow, double textWidth, double textHeight) {
		double textX = pointArrow.getX() + RelationPoints.POINT_SELECTION_RADIUS; // default x-pos is at the right end of selectionradius
		double textY = pointArrow.getY();
		
		// if text would be placed on the right outside of the relation and there is enough space to place it inside, do so
		double selectionDiameter = RelationPoints.POINT_SELECTION_RADIUS*2;
		double textXWithinRelationSpace = textX - textWidth - selectionDiameter;
		if (textXWithinRelationSpace > selectionDiameter) {
			textX = textXWithinRelationSpace;
		}
		
		// if text wouldn't fit on top of the relation (normally for arrow points which are on the upper most position of the relation), place it on bottom of it
		if (textY < textHeight) {
			textY += textHeight; // make sure larger fontsizes will still fit
		}
		return new PointDouble(textX, textY);
	}

	private PointDouble calcPosOfMiddleText(PointDouble center, double textWidth, double textHeight) {
		double textX = center.getX() - textWidth/2;
		double textY = center.getY();

		// if text would not be visible at the left relation part, move it to visible area
		if (textX < 0) {
			textX += textWidth/2 + RelationPoints.DRAG_BOX_SIZE;
		}

		return new PointDouble(textX, textY);
	}

}
