package com.baselet.elementnew.facet.specific;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.GlobalFacet;
import com.baselet.gui.AutocompletionText;

public class ActiveClassFacet extends GlobalFacet {

	public static ActiveClassFacet INSTANCE = new ActiveClassFacet();
	private ActiveClassFacet() {}

	private static final String KEY = "{active}";

	private static final int SPACING = 6;

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return line.equals(KEY);
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		state.addToHorizontalBuffer(SPACING);
		XValues xLimits = state.getXLimits(state.getyPos());
		drawer.drawLine(xLimits.getLeft(), 0, xLimits.getLeft(), state.getGridElementSize().getHeight());
		drawer.drawLine(xLimits.getRight(), 0, xLimits.getRight(), state.getGridElementSize().getHeight());
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(KEY, "make class active (double left/right border)"));
	}
	
	@Override
	public Priority getPriority() {
		return Priority.HIGH; // because it changes the xlimits
	}

}
