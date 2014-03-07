package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.PointDoubleStickingPolygonGenerator;
import com.baselet.element.sticking.SimpleStickingPolygonGenerator;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.Facet;
import com.umlet.element.experimental.facet.common.SeparatorLineFacet;
import com.umlet.element.experimental.facet.specific.ActiveClassFacet;
import com.umlet.element.experimental.facet.specific.InnerClassFacet;
import com.umlet.element.experimental.facet.specific.TemplateClassFacet;
import com.umlet.element.experimental.facet.specific.TitleFacet;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsClass;


public class Class extends NewGridElement {
	
	@Override
	protected Settings createSettings() {
		return new SettingsClass() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(InnerClassFacet.INSTANCE, SeparatorLineFacet.INSTANCE_WITH_HALIGN_CHANGE, ActiveClassFacet.INSTANCE, TemplateClassFacet.INSTANCE, TitleFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLClass;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		Rectangle tR = propCfg.getFacetResponse(TemplateClassFacet.class, null);
		int height = getRealSize().getHeight() - 1;
		int width = getRealSize().getWidth() - 1;
		if (tR == null) {
			drawer.drawRectangle(0, 0, width, height);
			propCfg.setStickingPolygonGenerator(SimpleStickingPolygonGenerator.INSTANCE);
			
		} else {
			List<PointDouble> points = TemplateClassFacet.drawTemplateClass(drawer, tR, height, width);
			propCfg.setStickingPolygonGenerator(new PointDoubleStickingPolygonGenerator(points));
		}
	}

}

