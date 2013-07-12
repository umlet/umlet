package com.umlet.element.experimental.element.uml.relation;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.GlobalStatelessFacet;
import com.umlet.element.experimental.settings.SettingsRelation;

public abstract class RelationFacet extends GlobalStatelessFacet {

	public final void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		RelationPoints rp = ((SettingsRelation) propConfig.getSettings()).getRelationPoints();
		handleLine(line, drawer, propConfig, rp);
	}
	
	abstract void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig, RelationPoints relationPoints);

}
