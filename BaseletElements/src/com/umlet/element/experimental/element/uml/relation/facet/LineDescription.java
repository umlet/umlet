package com.umlet.element.experimental.element.uml.relation.facet;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.element.uml.relation.RelationPoints;

public class LineDescription extends RelationFacet {

	private static final String MESSAGE_START_KEY = "lm";
	private static final String MESSAGE_END_KEY = "rm";
	@Override
	public boolean checkStart(String line) {
		return line.startsWith(MESSAGE_START_KEY + SEP) || line.startsWith(MESSAGE_END_KEY + SEP);
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {
				new AutocompletionText(MESSAGE_START_KEY + SEP, "message at start"),
				new AutocompletionText(MESSAGE_END_KEY + SEP, "message at end"),
				};
	}

	@Override
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig, RelationPoints relationPoints) {
		String[] split = line.split(SEP, -1);
		String key = split[0];
		String text = split[1];
		double textWidth = drawer.textWidth(text);
		double textHeight = drawer.textHeight();
		PointDouble pointArrow = null;
		if (!text.isEmpty()) {
			if (key.equals(MESSAGE_START_KEY)) {
				pointArrow = relationPoints.getFirstLine().getStart();
			} else if (key.equals(MESSAGE_END_KEY)) {
				pointArrow = relationPoints.getLastLine().getEnd();
			}
			PointDouble pointText = calcTextPos(pointArrow, textWidth, textHeight);
			drawer.print(text, pointText, AlignHorizontal.LEFT);
			
			// to make sure text is printed (and therefore withing relation-element-borders, resize relation according to text
			relationPoints.resizeRelationSpaceToMakeTextVisible(textWidth+pointText.getX(), pointText.getY());
		}
	}

	private PointDouble calcTextPos(PointDouble pointArrow, double textWidth, double textHeight) {
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
		PointDouble pointText = new PointDouble(textX, textY);
		return pointText;
	}

}
