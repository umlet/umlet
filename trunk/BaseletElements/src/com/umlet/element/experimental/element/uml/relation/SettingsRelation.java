package com.umlet.element.experimental.element.uml.relation;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.Settings;
import com.umlet.element.experimental.element.uml.relation.facet.Arrow;
import com.umlet.element.experimental.element.uml.relation.facet.LineDescription;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.GlobalFacet;
import com.umlet.element.experimental.facets.defaults.BackgroundColorFacet;
import com.umlet.element.experimental.facets.defaults.FontSizeFacet;
import com.umlet.element.experimental.facets.defaults.ForegroundColorFacet;
import com.umlet.element.experimental.facets.defaults.LayerFacet;
import com.umlet.element.experimental.facets.defaults.LineThicknessFacet;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;

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
	public Facet[] createFacets() {
		return new Facet[] {new Arrow(), new LineDescription()};
	}
	@Override
	protected GlobalFacet[] createDefaultGlobalFacets() {
		return new GlobalFacet[] {new BackgroundColorFacet(), new FontSizeFacet(), new ForegroundColorFacet(), new LayerFacet(), new LineThicknessFacet()};
	}

}
