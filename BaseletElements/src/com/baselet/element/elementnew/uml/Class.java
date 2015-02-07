package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.SeparatorLineWithHalignChangeFacet;
import com.baselet.element.facet.specific.ActiveClassFacet;
import com.baselet.element.facet.specific.InnerClassFacet;
import com.baselet.element.facet.specific.TemplateClassFacet;
import com.baselet.element.settings.SettingsManualResizeTop;
import com.baselet.element.sticking.polygon.PointDoubleStickingPolygonGenerator;
import com.baselet.element.sticking.polygon.SimpleStickingPolygonGenerator;

public class Class extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualResizeTop() {
			@Override
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), InnerClassFacet.INSTANCE, SeparatorLineWithHalignChangeFacet.INSTANCE, ActiveClassFacet.INSTANCE, TemplateClassFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLClass;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		String templateClassValue = state.getFacetResponse(TemplateClassFacet.class, "");
		int height = getRealSize().getHeight();
		int width = getRealSize().getWidth();
		if (templateClassValue.isEmpty()) {
			drawer.drawRectangle(0, 0, width, height);
			state.setStickingPolygonGenerator(SimpleStickingPolygonGenerator.INSTANCE);
		}
		else {
			List<PointDouble> points = TemplateClassFacet.drawTemplateClass(templateClassValue, drawer, state, height, width);
			state.setStickingPolygonGenerator(new PointDoubleStickingPolygonGenerator(points));
		}
	}

}
