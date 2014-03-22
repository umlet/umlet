package com.baselet.elementnew.facet.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.gui.AutocompletionText;

public class SeparatorLineFacet extends Facet {

	public static SeparatorLineFacet INSTANCE = new SeparatorLineFacet(false);
	public static SeparatorLineFacet INSTANCE_WITH_HALIGN_CHANGE = new SeparatorLineFacet(true);

	private static final String KEY = "-";
	private static final List<LineType> okLineTypes = Arrays.asList(LineType.SOLID, LineType.DASHED, LineType.DOTTED);
	
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
		LineType previousLt = drawer.getStyle().getLineType();
		for (LineType lt : okLineTypes) {
			if (line.equals(KEY + lt.getValue())) {
				drawer.setLineType(lt);
			}
		}
		drawer.drawLine(xPos.getLeft()+0.5, linePos, xPos.getRight()-1, linePos);
		drawer.setLineType(previousLt);
		state.addToYPos(H_SPACE);
	}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		if (line.equals(KEY)) return true;
		for (LineType lt : okLineTypes) {
			if (line.equals(KEY + lt.getValue())) return true;
		}
		return false;
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
		returnList.add(new AutocompletionText(KEY, "draw horizontal line with current linetype"));
		for (LineType lt : okLineTypes) {
			returnList.add(new AutocompletionText(KEY + lt.getValue(), "draw " + lt.name().toLowerCase() + " horizontal line"));
		}
		return returnList;
	}

}
