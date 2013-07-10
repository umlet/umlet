package com.umlet.element.experimental.settings;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.settings.facets.Facet;
import com.umlet.element.experimental.settings.facets.SeparatorLine;
import com.umlet.element.experimental.settings.facets.DefaultGlobalTextFacet.ElementStyleEnum;

public class SettingsUseCase extends Settings {

	@Override
	public XValues getXValues(double y, int height, int width) {
		double b = height/2.0f;
		double a = width/2.0f;
		double x = Math.sqrt((1-(Math.pow(b-y, 2) / Math.pow(b, 2)))*Math.pow(a, 2));
		return new XValues(a-x, a+x);
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
	public ElementStyleEnum getElementStyle() {
		return ElementStyleEnum.RESIZE;
	}

	@Override
	public Facet[] createFacets() {
		return new Facet[]{new SeparatorLine()};
	}
}
