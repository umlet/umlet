package com.baselet.elementnew.settings;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.ElementStyleFacet.ElementStyleEnum;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;

public class SettingsAutoresize extends Settings {
	@Override
	public XValues getXValues(double y, int height, int width) {
		return new XValues(0, width);
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
	public ElementStyleEnum getElementStyle() {
		return ElementStyleEnum.AUTORESIZE;
	}

	@Override
	protected List<? extends Facet> createDefaultFacets() {
		return Settings.AUTORESIZE;
	}

	@Override
	public List<? extends Facet> createFacets() {
		return Arrays.asList(SeparatorLineFacet.INSTANCE);
	}
}
