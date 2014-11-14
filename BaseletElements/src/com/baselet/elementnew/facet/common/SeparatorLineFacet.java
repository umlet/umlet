package com.baselet.elementnew.facet.common;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.geom.XValues;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class SeparatorLineFacet extends Facet {

	public static final SeparatorLineFacet INSTANCE = new SeparatorLineFacet();

	protected SeparatorLineFacet() {}

	public static final String KEY = "--";

	private static final double Y_SPACE = 5;

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		double linePos = state.getyPos() - drawer.textHeightMax() + Y_SPACE / 2;
		XValues xPos = state.getXLimits(linePos);
		drawer.drawLine(xPos.getLeft() + 0.5, linePos, xPos.getRight() - 1, linePos);
		state.addToYPos(Y_SPACE);
	}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return line.equals(KEY);
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(KEY, "draw horizontal line"));
	}

}
