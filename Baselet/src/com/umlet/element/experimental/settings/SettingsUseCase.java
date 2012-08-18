package com.umlet.element.experimental.settings;

import java.util.Collection;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.umlet.element.experimental.settings.text.Facet;
import com.umlet.element.experimental.settings.text.SeparatorLine;

public class SettingsUseCase extends Settings {

	@Override
	public XPoints getXValues(float y, int height, int width) {
		float b = height/2;
		float a = width/2;
		int x = (int) Math.sqrt((1-(Math.pow(b-y, 2) / Math.pow(b, 2)))*Math.pow(a, 2));
		return new XPoints(a-x, a+x);
	}

	@Override
	public AlignVertical getVAlign() {
		return AlignVertical.CENTER;
	}

	@Override
	public AlignHorizontal getHAlign() {
		return AlignHorizontal.CENTER;
	}

	@Override
	public Facet[] createFacets() {
		return new Facet[]{new SeparatorLine(false)};
	}
}
