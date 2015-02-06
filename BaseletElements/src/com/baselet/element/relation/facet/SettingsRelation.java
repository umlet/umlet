package com.baselet.element.relation.facet;

import java.util.Arrays;
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
	public List<? extends Facet> createFacets() {
		return Arrays.asList(RelationLineTypeFacet.INSTANCE, LineDescriptionFacet.INSTANCE, DescriptionPositionFacet.INSTANCE_MESSAGE_START, DescriptionPositionFacet.INSTANCE_MESSAGE_END, DescriptionPositionFacet.INSTANCE_ROLE_START, DescriptionPositionFacet.INSTANCE_ROLE_END);
	}

	@Override
	protected List<? extends Facet> createDefaultFacets() {
		return Settings.RELATION;
	}

	@Override
	public boolean printText() {
		return false;
	}

	public abstract RelationPointHandler getRelationPoints();

}
