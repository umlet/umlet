package com.umlet.element.experimental.settings;

import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;

public abstract class SettingsNoText extends Settings {
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
		return ElementStyleEnum.NORESIZE;
	}
	@Override
	protected List<? extends Facet> createDefaultFacets() {
		return Settings.NOTEXT;
	}
	@Override
	public boolean printText() {
		return false;
	}
}
