package com.baselet.element.settings;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.geom.XValues;
import com.baselet.element.facet.ElementStyleEnum;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.common.SeparatorLineFacet;

public class SettingsAutoresize extends SettingsAbstract {
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
		return SettingsAbstract.AUTORESIZE;
	}

	@Override
	public List<? extends Facet> createFacets() {
		return Arrays.asList(SeparatorLineFacet.INSTANCE);
	}
}
