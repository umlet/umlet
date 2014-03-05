package com.umlet.element.experimental.settings;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.base.SeparatorLine;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;

public class SettingsText extends Settings {
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
		return AlignHorizontal.LEFT;
	}
	@Override
	public ElementStyleEnum getElementStyle() {
		return ElementStyleEnum.WORDWRAP;
	}
	@Override
	public List<? extends Facet> createFacets() {
		return Arrays.asList(SeparatorLine.INSTANCE);
	}
	@Override
	protected List<? extends Facet> createDefaultFacets() {
		return Settings.ALL;
	}
}
