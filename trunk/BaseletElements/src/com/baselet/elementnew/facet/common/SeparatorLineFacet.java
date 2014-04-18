package com.baselet.elementnew.facet.common;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.gui.AutocompletionText;

public class SeparatorLineFacet extends Facet {

	public static SeparatorLineFacet INSTANCE = new SeparatorLineFacet(false);
	public static SeparatorLineFacet INSTANCE_WITH_HALIGN_CHANGE = new SeparatorLineFacet(true);

	private static final String KEY = "--";
	
	private boolean setHAlignToLeftAfterLine;
	private static final int H_SPACE = 4;

	private SeparatorLineFacet(boolean setHAlignToLeftAfterLine) {
		this.setHAlignToLeftAfterLine = setHAlignToLeftAfterLine;
	}
	
	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		if (setHAlignToLeftAfterLine) {
			state.sethAlign(AlignHorizontal.LEFT);
		}
		double linePos = state.getDividerPos(drawer);
		XValues xPos = state.getXLimits(linePos);
		drawer.drawLine(xPos.getLeft()+0.5, linePos, xPos.getRight()-1, linePos);
		state.addToYPos(H_SPACE);
	}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		if (line.equals(KEY)) return true;
		return false;
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(KEY, "draw horizontal line"));
	}

}
