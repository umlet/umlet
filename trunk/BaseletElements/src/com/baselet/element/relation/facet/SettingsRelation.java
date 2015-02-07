package com.baselet.element.relation.facet;

import java.util.List;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.element.facet.ElementStyleEnum;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.Settings;
import com.baselet.element.relation.helper.RelationPointHandler;

public abstract class SettingsRelation extends Settings {

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
	protected List<Facet> createFacets() {
		return Settings.RELATION;
	}

	@Override
	public boolean printText() {
		return false;
	}

	public abstract RelationPointHandler getRelationPoints();

}
