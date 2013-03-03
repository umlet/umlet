package com.umlet.element.experimental.settings;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.umlet.element.experimental.helper.XPoints;
import com.umlet.element.experimental.settings.facets.Facet;
import com.umlet.element.experimental.settings.facets.SeparatorLine;

public class SettingsUseCase extends Settings {

	@Override
	public XPoints getXValues(float y, int height, int width) {
		float b = height/2.0f;
		float a = width/2.0f;
		float x = (float) Math.sqrt((1-(Math.pow(b-y, 2) / Math.pow(b, 2)))*Math.pow(a, 2));
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
