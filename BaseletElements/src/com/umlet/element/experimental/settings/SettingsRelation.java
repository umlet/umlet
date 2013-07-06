package com.umlet.element.experimental.settings;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.settings.facets.ActiveClass;
import com.umlet.element.experimental.settings.facets.Facet;
import com.umlet.element.experimental.settings.facets.InnerClass;
import com.umlet.element.experimental.settings.facets.SeparatorLine;
import com.umlet.element.experimental.settings.facets.TemplateClass;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.ElementStyleEnum;
import com.umlet.element.experimental.uml.relation.ArrowEnd;
import com.umlet.element.experimental.uml.relation.ArrowStart;
import com.umlet.element.experimental.uml.relation.RelationPoints;

public class SettingsRelation extends Settings {

	private RelationPoints relationPoints;

	public SettingsRelation(RelationPoints relationPoints) {
		this.relationPoints = relationPoints;
	}
	
	public RelationPoints getRelationPoints() {
		return relationPoints;
	}

	@Override
	public XValues getXValues(float y, int height, int width) {
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
		return new Facet[] {new ArrowStart(), new ArrowEnd()};
	}

}
