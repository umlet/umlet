package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
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
				return Arrays.asList(new InnerClassFacet(), SeparatorLineFacet.INSTANCE_WITH_HALIGN_CHANGE, ActiveClassFacet.INSTANCE, TemplateClassFacet.INSTANCE, TitleFacet.INSTANCE);
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
		} else {
			int us = tR.getHeight()/2;
			int rightEnd = width-tR.getWidth()/2;
			// DRAW BACKGROUND RECT
			Style style = drawer.getCurrentStyle();
			drawer.setForegroundColor(ColorOwn.TRANSPARENT);
			PointDouble p1 = p(0, us);
			PointDouble p2 = p(tR.getX(), us);
			PointDouble p3 = p(tR.getX(), 0);
			PointDouble p4 = p(width, 0);
			PointDouble p5 = p(width, tR.getHeight());
			PointDouble p6 = p(rightEnd, tR.getHeight());
			PointDouble p7 = p(rightEnd, height);
			PointDouble p8 = p(0, height);
			drawer.drawLines(p1, p2, p3, p4, p5, p6, p7, p8, p1);
			drawer.setStyle(style.cloneFromMe()); // reset style to state before manipulations
			// DRAW RIGHT RECT
			drawer.setLineType(LineType.DASHED);
			drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
			drawer.drawRectangle(tR);
			drawer.setStyle(style); // reset style to state before manipulations
			// DRAW REST OF CLASS
			drawer.drawLines(p2, p1, p8, p7, p6);
		}
	}

	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}


}

