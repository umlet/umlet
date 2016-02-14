package com.baselet.element.relation.facet;

import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.Direction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.relation.helper.LineDescriptionEnum;
import com.baselet.element.relation.helper.RelationPointHandler;

public class LineDescriptionUtils {

	static PointDouble calcPosOfLineDescriptionText(DrawHandler drawer, String text, int lineNr, int totalLines, RelationPointHandler relationPoints, LineDescriptionEnum enumVal) {
		double textWidth = calcWidth(drawer, text);
		double totalBlock = totalLines * drawer.textHeightMax();
		double previousLinesBlock = lineNr * drawer.textHeightMax();

		Boolean printOnStart = enumVal.isStart();
		Line line = printOnStart ? relationPoints.getFirstLine() : relationPoints.getLastLine();
		PointDouble pointText = line.getPointOnLineWithDistanceFrom(printOnStart, 15); // distance from lineend (because of arrows,...)
		Direction lineDirection = line.getDirectionOfLine(printOnStart);

		// Default Positioning
		if (lineDirection == Direction.RIGHT) {
			pointText = new PointDouble(pointText.getX() - textWidth - drawer.getDistanceBorderToText(), pointText.getY() + drawer.textHeightMax() + previousLinesBlock + LineDescriptionFacet.LOWER_Y_DIST_TO_LINE);
		}
		else if (lineDirection == Direction.LEFT) {
			pointText = new PointDouble(pointText.getX() + LineDescriptionFacet.X_DIST_TO_LINE, pointText.getY() + drawer.textHeightMax() + previousLinesBlock + LineDescriptionFacet.LOWER_Y_DIST_TO_LINE);
		}
		else if (lineDirection == Direction.UP) {
			pointText = new PointDouble(pointText.getX() + LineDescriptionFacet.X_DIST_TO_LINE, pointText.getY() + drawer.textHeightMax() + previousLinesBlock + LineDescriptionFacet.LOWER_Y_DIST_TO_LINE);
		}
		else if (lineDirection == Direction.DOWN) {
			pointText = new PointDouble(pointText.getX() + LineDescriptionFacet.X_DIST_TO_LINE, pointText.getY() + drawer.textHeightMax() - totalBlock + previousLinesBlock - LineDescriptionFacet.LOWER_Y_DIST_TO_LINE);
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
				pointText = new PointDouble(pointText.getX(), pointText.getY() - totalLines * drawer.textHeightMax() - LineDescriptionFacet.UPPER_Y_DIST_TO_LINE);
			}
		}
		return pointText;
	}

	private static double calcWidth(DrawHandler drawer, String... text) {
		double maxWidth = 0;
		for (String line : text) {
			maxWidth = Math.max(maxWidth, drawer.textWidth(line));
		}
		return maxWidth;
	}

	static String replaceArrowsWithUtf8Characters(String text) {
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

	static PointDouble calcPosOfMiddleText(DrawHandler drawer, String text, Line line, int currentLineNr, double halfMiddleBlockHeight) {
		double textWidth = calcWidth(drawer, text);
		boolean horizontalLine = line.getDirectionOfLine(true).isHorizontal();
		PointDouble center = line.getCenter();

		double textX, textY;
		double previousLinesUsedSpace = currentLineNr * drawer.textHeightMaxWithSpace();
		if (horizontalLine) {
			textX = center.getX() - textWidth / 2;
			textY = center.getY() + previousLinesUsedSpace - LineDescriptionFacet.MIDDLE_DISTANCE_TO_LINE;
		}
		else {
			textX = center.getX() + LineDescriptionFacet.X_DIST_TO_LINE;
			textY = center.getY() + previousLinesUsedSpace - halfMiddleBlockHeight + drawer.textHeightMaxWithSpace(); // must use textHeightMaxWithSpace and not the height of the line to make sure the text looks good (see Issue 235)
		}
		return new PointDouble(textX, textY);
	}

}
