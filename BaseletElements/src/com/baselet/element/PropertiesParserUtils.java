package com.baselet.element;

import com.baselet.control.enums.ElementStyle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;
import com.baselet.element.facet.PropertiesParserState;

public class PropertiesParserUtils {

	/**
	 * Calculates the necessary y-pos space to make the first line fit the xLimits of the element
	 * Currently only used by UseCase element to make sure the first line is moved down as long as it doesn't fit into the available space
	 */
	static double calcTopDisplacementToFitLine(String firstLine, PropertiesParserState state, DrawHandler drawer) {
		double displacement = 0;
		boolean wordwrap = state.getElementStyle() == ElementStyle.WORDWRAP;
		if (!wordwrap) { // in case of wordwrap or no text, there is no top displacement
			int BUFFER = 2; // a small buffer between text and outer border
			double textHeight = drawer.textHeightMax();
			double addedSpacePerIteration = textHeight / 2;
			double yPosToCheck = state.getyPos();
			double availableWidthSpace = state.getXLimitsForArea(yPosToCheck, textHeight, true).getSpace() - BUFFER;
			int maxLoops = 1000;
			while (yPosToCheck < state.getGridElementSize().height && !TextSplitter.checkifStringFits(firstLine, availableWidthSpace, drawer)) {
				if (maxLoops-- < 0) {
					throw new RuntimeException("Endless loop during calculation of top displacement");
				}
				yPosToCheck += addedSpacePerIteration;
				double previousWidthSpace = availableWidthSpace;
				availableWidthSpace = state.getXLimitsForArea(yPosToCheck, textHeight, true).getSpace() - BUFFER;
				// only set displacement if the last iteration resulted in a space gain (eg: for UseCase until the middle, for Class: stays on top because on a rectangle there is never a width-space gain)
				if (availableWidthSpace > previousWidthSpace) {
					displacement += addedSpacePerIteration;
				}
			}
		}
		return displacement;
	}
}
