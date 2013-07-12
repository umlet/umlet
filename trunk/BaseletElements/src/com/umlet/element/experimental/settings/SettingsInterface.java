package com.umlet.element.experimental.settings;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.SeparatorLine;
import com.umlet.element.experimental.facets.DefaultGlobalTextFacet.ElementStyleEnum;

public class SettingsInterface extends Settings {

	@Override
	public XValues getXValues(double y, int height, int width) {
		return new XValues(0, width);
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
		return ElementStyleEnum.AUTORESIZE;
	}
	
	@Override
	public double getYPosStart() {
		return 22; // space reserved for the top circle
	}

	@Override
	public Facet[] createFacets() {
		return new Facet[]{new SeparatorLine()};
	}

}
