package com.baselet.elementnew.settings;

import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.ElementStyleFacet.ElementStyleEnum;

public abstract class SettingsManualresizeCenter extends Settings {
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
		return ElementStyleEnum.SIMPLE;
	}

	@Override
	protected List<? extends Facet> createDefaultFacets() {
		return Settings.ALL;
	}
}
