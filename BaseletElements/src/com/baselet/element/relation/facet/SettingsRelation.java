package com.baselet.element.relation.facet;

import java.util.List;

import com.baselet.element.facet.ElementStyleEnum;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.Settings;
import com.baselet.element.relation.helper.RelationPointHandler;

public abstract class SettingsRelation extends Settings {

	@Override
	public ElementStyleEnum getElementStyle() {
		return ElementStyleEnum.NORESIZE;
	}

	@Override
	protected List<Facet> createFacets() {
		return Settings.RELATION;
	}

	public abstract RelationPointHandler getRelationPoints();

}
