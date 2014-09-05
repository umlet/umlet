package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualResizeTop;

public class Deployment extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualResizeTop() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLineFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLDeployment;
	}

	private static final int BORDER = 10;

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		double w = getRealSize().getWidth();
		double h = getRealSize().getHeight();
		List<PointDouble> p = Arrays.asList(
				new PointDouble(0, BORDER),
				new PointDouble(BORDER, 0),
				new PointDouble(w, 0),
				new PointDouble(w, h - BORDER),
				new PointDouble(w - BORDER, h)
				);
		PointDouble pLine = new PointDouble(w - BORDER, BORDER);
		// Fill 3d-rectangle
		Style oldStyle = drawer.getStyle().cloneFromMe();
		drawer.setForegroundColor(ColorOwn.TRANSPARENT);
		if (oldStyle.getBackgroundColor() == ColorOwn.DEFAULT_BACKGROUND) {
			drawer.setBackgroundColor(ColorOwn.WHITE.transparency(Transparency.BACKGROUND).darken(80));
		}
		else {
			drawer.setBackgroundColor(oldStyle.getBackgroundColor().darken(80));
		}
		drawer.drawLines(p.get(0), p.get(1), p.get(2), p.get(3), p.get(4), pLine, p.get(0));
		drawer.setStyle(oldStyle);
		// Draw 3d-rectangle border
		drawer.drawLines(p);
		drawer.drawLines(pLine, p.get(2));
		// Draw Content-Rectangle
		drawer.drawRectangle(0, BORDER, w - BORDER, h - BORDER);
		state.addToTopBuffer(BORDER);
		state.addToRightBuffer(BORDER);
	}

}
