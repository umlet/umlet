package com.baselet.element;

import java.util.List;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;
import com.baselet.element.facet.ElementStyleEnum;
import com.baselet.element.facet.PropertiesParserState;

public class PropertiesParserUtils {

	static double calcTopDisplacementToFitLine(List<String> propertiesText, double startPoint, PropertiesParserState state, DrawHandler drawer) {
		double displacement = startPoint;
		boolean wordwrap = state.getElementStyle() == ElementStyleEnum.WORDWRAP;
		if (!wordwrap && !propertiesText.isEmpty()) { // in case of wordwrap or no text, there is no top displacement
			int BUFFER = 2; // a small buffer between text and outer border
			double textHeight = drawer.textHeightMax();
			String firstLine = propertiesText.iterator().next();
			double availableWidthSpace = state.getXLimitsForArea(displacement, textHeight, true).getSpace() - BUFFER;
			double accumulator = displacement;
			int maxLoops = 1000;
			while (accumulator < state.getGridElementSize().height && !TextSplitter.checkifStringFits(firstLine, availableWidthSpace, drawer)) {
				if (maxLoops-- < 0) {
					throw new RuntimeException("Endless loop during calculation of top displacement");
				}
				accumulator += textHeight / 2;
				double previousWidthSpace = availableWidthSpace;
				availableWidthSpace = state.getXLimitsForArea(accumulator, textHeight, true).getSpace() - BUFFER;
				// only set displacement if the last iteration resulted in a space gain (eg: for UseCase until the middle, for Class: stays on top because on a rectangle there is never a width-space gain)
				if (availableWidthSpace > previousWidthSpace) {
					displacement = accumulator;
				}
			}
		}
		return displacement;
	}

	static double calcHorizontalTextBoundaries(XValues xLimitsForText, double distanceBorderToText, AlignHorizontal hAlign) {
		double x;
		if (hAlign == AlignHorizontal.LEFT) {
			x = xLimitsForText.getLeft() + distanceBorderToText;
		}
		else if (hAlign == AlignHorizontal.CENTER) {
			x = xLimitsForText.getSpace() / 2.0 + xLimitsForText.getLeft();
		}
		else /* if (state.gethAlign() == AlignHorizontal.RIGHT) */{
			x = xLimitsForText.getRight() - distanceBorderToText;
		}
		return x;
	}
}
