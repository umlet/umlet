package com.umlet.element.experimental.settings;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;

public class SettingsClass implements Settings {

	@Override
	public float[] getXValues(float y, int height, int width) {
		return new float[] {0, width};
	}

	@Override
	public AlignVertical getVAlign() {
		return AlignVertical.TOP;
	}

	@Override
	public AlignHorizontal getHAlignBeforeLine() {
		return AlignHorizontal.CENTER;
	}

	@Override
	public AlignHorizontal getHAlignAfterLine() {
		return AlignHorizontal.LEFT;
	}

}
