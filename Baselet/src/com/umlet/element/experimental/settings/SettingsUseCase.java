package com.umlet.element.experimental.settings;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;

public class SettingsUseCase implements Settings {

	@Override
	public float[] getXValues(float y, int height, int width) {
		float b = height/2;
		float a = width/2;
		int x = (int) Math.sqrt((1-(Math.pow(b-y, 2) / Math.pow(b, 2)))*Math.pow(a, 2));
		return new float[] {a-x, a+x};
	}

	@Override
	public AlignVertical getVAlign() {
		return AlignVertical.CENTER;
	}

	@Override
	public AlignHorizontal getHAlignBeforeLine() {
		return AlignHorizontal.CENTER;
	}

	@Override
	public AlignHorizontal getHAlignAfterLine() {
		return AlignHorizontal.CENTER;
	}
}
