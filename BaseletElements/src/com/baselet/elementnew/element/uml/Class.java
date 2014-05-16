package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.element.sticking.polygon.PointDoubleStickingPolygonGenerator;
import com.baselet.element.sticking.polygon.SimpleStickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineWithHalignChangeFacet;
import com.baselet.elementnew.facet.specific.ActiveClassFacet;
import com.baselet.elementnew.facet.specific.InnerClassFacet;
import com.baselet.elementnew.facet.specific.TemplateClassFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualResizeTop;

public class Class extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualResizeTop() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(InnerClassFacet.INSTANCE, SeparatorLineWithHalignChangeFacet.INSTANCE, ActiveClassFacet.INSTANCE, TemplateClassFacet.INSTANCE);
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
