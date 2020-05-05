package com.baselet.element.elementnew.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwnBase;
import com.baselet.diagram.draw.helper.ColorOwnBase.Transparency;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.SeparatorLineFacet;
import com.baselet.element.settings.SettingsManualResizeTop;

public class Deployment extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualResizeTop() {
			@Override
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), SeparatorLineFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLDeployment;
	}

	private static final int BORDER = 10;

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		double w = getRealSize().getWidth();
		double h = getRealSize().getHeight();
		List<PointDouble> p = Arrays.asList(
				new PointDouble(0, BORDER),
				new PointDouble(BORDER, 0),
				new PointDouble(w, 0),
				new PointDouble(w, h - BORDER),
				new PointDouble(w - BORDER, h));
		PointDouble pLine = new PointDouble(w - BORDER, BORDER);
		// Fill 3d-rectangle
		Style oldStyle = drawer.getStyleClone();
		ColorOwnBase currentColor = Theme.getCurrentThemeColor();
		drawer.setForegroundColor(currentColor.getColorMap().get(ColorOwnBase.PredefinedColors.TRANSPARENT));
		if (oldStyle.getBackgroundColor() == currentColor.getStyleColorMap().get(ColorOwnBase.ColorStyle.DEFAULT_BACKGROUND)) {
			drawer.setBackgroundColor(currentColor.getColorMap().get(ColorOwnBase.PredefinedColors.WHITE).transparency(Transparency.BACKGROUND).darken(80));
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
		state.getBuffer().setTopMin(BORDER);
		state.getBuffer().addToRight(BORDER);
	}

}
