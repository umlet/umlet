package com.baselet.element.facet.common;

import java.util.Collections;
import java.util.List;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class TextPrintFacet extends Facet {

	public static final TextPrintFacet INSTANCE = new TextPrintFacet();

	private TextPrintFacet() {}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return true;
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		XValues xLimitsForText = state.getXLimitsForArea(state.getyPos(), drawer.textHeightMax(), false);
		Double spaceNotUsedForText = state.getGridElementSize().width - xLimitsForText.getSpace();
		if (!spaceNotUsedForText.equals(Double.NaN)) { // NaN is possible if xlimits calculation contains e.g. a division by zero
			state.updateCalculatedElementWidth(spaceNotUsedForText + drawer.textWidth(line));
		}
		AlignHorizontal hAlign = state.gethAlign();
		drawer.print(line, calcHorizontalTextBoundaries(xLimitsForText, drawer.getDistanceBorderToText(), hAlign), state.getyPos(), hAlign);
		state.addToYPos(drawer.textHeightMaxWithSpace());
	}

	private static double calcHorizontalTextBoundaries(XValues xLimitsForText, double distanceBorderToText, AlignHorizontal hAlign) {
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

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Collections.emptyList();
	}

	@Override
	public Priority getPriority() {
		return Priority.LOWEST;
	}

}
