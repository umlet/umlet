package com.umlet.element.experimental.uml.relation;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.settings.SettingsRelation;
import com.umlet.element.experimental.settings.facets.GlobalStatelessFacet;

public abstract class RelationFacet extends GlobalStatelessFacet {

	public final void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		RelationPoints rp = ((SettingsRelation) propConfig.getSettings()).getRelationPoints();
		handleLine(line, drawer, propConfig, rp);
	}
	
	abstract void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig, RelationPoints relationPoints);

}
