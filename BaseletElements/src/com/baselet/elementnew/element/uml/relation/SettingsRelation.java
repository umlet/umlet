package com.baselet.elementnew.element.uml.relation;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.ElementStyleFacet.ElementStyleEnum;
import com.baselet.elementnew.facet.relation.DescriptionPositionFacet;
import com.baselet.elementnew.facet.relation.LineDescriptionFacet;
import com.baselet.elementnew.facet.relation.RelationLineTypeFacet;
import com.baselet.elementnew.settings.Settings;

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
		return Arrays.asList(RelationLineTypeFacet.INSTANCE, LineDescriptionFacet.INSTANCE, DescriptionPositionFacet.INSTANCE_START, DescriptionPositionFacet.INSTANCE_END);
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
