package com.umlet.element.experimental.settings.text;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;

public class SeparatorLine implements Facet {

	private boolean setHAlignToLeftAfterLine;

	public SeparatorLine() {
		this(false);
	}

	public SeparatorLine(boolean setHAlignToLeftAfterLine) {
		this.setHAlignToLeftAfterLine = setHAlignToLeftAfterLine;
	}
	
	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		if (setHAlignToLeftAfterLine && !propConfig.ishAlignFixed()) {
			propConfig.sethAlign(AlignHorizontal.LEFT);
		}
		float linePos = propConfig.getyPos() - (drawer.textHeight()) + 2;
		float[] xPos = propConfig.getXLimits(linePos);
		drawer.drawLine(xPos[0]+1, linePos, xPos[1]-1, linePos);
		propConfig.addToYPos(getHorizontalSpace());
	}

	@Override
	public boolean checkStart(String line) {
		return line.equals("--");
	}

	@Override
	public float getHorizontalSpace() {
		return 4;
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

}
