package com.umlet.element.experimental.settings;

import java.util.Collection;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.umlet.element.experimental.settings.text.Facet;
import com.umlet.element.experimental.settings.text.SeparatorLineWithHalignSwitch;

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
	public AlignHorizontal getHAlign() {
		return AlignHorizontal.CENTER;
	}

	@Override
	public Facet[] getFacets() {
		return new Facet[]{new SeparatorLineWithHalignSwitch(true)};
	}

}
