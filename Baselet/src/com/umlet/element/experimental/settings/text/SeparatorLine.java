package com.umlet.element.experimental.settings.text;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.helper.XPoints;

public class SeparatorLine implements Facet {

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
		Helper.drawHorizontalLine(H_SPACE, drawer, propConfig);
	}

	@Override
	public boolean checkStart(String line) {
		return line.equals("--");
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

}
