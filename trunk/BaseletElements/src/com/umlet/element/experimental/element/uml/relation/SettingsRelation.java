package com.umlet.element.experimental.element.uml.relation;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facet.Facet;
import com.umlet.element.experimental.facet.common.ElementStyleFacet.ElementStyleEnum;
import com.umlet.element.experimental.facet.relation.LineDescriptionFacet;
import com.umlet.element.experimental.facet.relation.RelationLineTypeFacet;
import com.umlet.element.experimental.settings.Settings;

public class SettingsRelation extends Settings {

	private RelationPoints relationPoints;

	public SettingsRelation(RelationPoints relationPoints) {
		this.relationPoints = relationPoints;
	}
	public RelationPoints getRelationPoints() {
		return relationPoints;
	}
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
		return Arrays.asList(RelationLineTypeFacet.INSTANCE, LineDescriptionFacet.INSTANCE);
	}
	@Override
	protected List<? extends Facet> createDefaultFacets() {
		return Settings.RELATION;
	}
	@Override
	public boolean printText() {
		return false;
	}

}
