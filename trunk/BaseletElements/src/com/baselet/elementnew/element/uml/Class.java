package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.element.sticking.PointDoubleStickingPolygonGenerator;
import com.baselet.element.sticking.SimpleStickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.facet.specific.ActiveClassFacet;
import com.baselet.elementnew.facet.specific.InnerClassFacet;
import com.baselet.elementnew.facet.specific.TemplateClassFacet;
import com.baselet.elementnew.facet.specific.TitleFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsClass;

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
		String templateClassValue = propCfg.getFacetResponse(TemplateClassFacet.class, "");
		int height = getRealSize().getHeight();
		int width = getRealSize().getWidth();
		if (templateClassValue.isEmpty()) {
			drawer.drawRectangle(0, 0, width, height);
			propCfg.setStickingPolygonGenerator(SimpleStickingPolygonGenerator.INSTANCE);
		}
		else {
			List<PointDouble> points = TemplateClassFacet.drawTemplateClass(templateClassValue, drawer, propCfg, height, width);
			propCfg.setStickingPolygonGenerator(new PointDoubleStickingPolygonGenerator(points));
		}
	}

}
