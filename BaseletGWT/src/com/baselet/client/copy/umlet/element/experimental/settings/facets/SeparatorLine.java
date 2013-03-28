package com.baselet.client.copy.umlet.element.experimental.settings.facets;

import com.baselet.client.copy.control.enumerations.AlignHorizontal;
import com.baselet.client.copy.diagram.draw.BaseDrawHandler;
import com.baselet.client.copy.diagram.draw.geom.LineHorizontal;
import com.baselet.client.copy.gui.AutocompletionText;
import com.baselet.client.copy.umlet.element.experimental.PropertiesConfig;

public class SeparatorLine implements Facet {

	private static final String KEY = "--";
	
	private boolean setHAlignToLeftAfterLine;
	private static final int H_SPACE = 4;

	public SeparatorLine() {
		this(false);
	}

	public SeparatorLine(boolean setHAlignToLeftAfterLine) {
		this.setHAlignToLeftAfterLine = setHAlignToLeftAfterLine;
	}
	
	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		if (setHAlignToLeftAfterLine) {
			propConfig.sethAlign(AlignHorizontal.LEFT);
		}
		float linePos = propConfig.getDividerPos(drawer.textHeight());
		LineHorizontal xPos = propConfig.getXLimits(linePos);
		drawer.drawLine(xPos.getLeft()+1, linePos, xPos.getRight()-1, linePos);
		propConfig.addToYPos(H_SPACE);
	}

	@Override
	public boolean checkStart(String line) {
		return line.equals(KEY);
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {new AutocompletionText(KEY, "draw horizontal line")};
	}

}
