package com.baselet.element.elementnew.uml;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.settings.SettingsText;

public class Note extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsText();
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLNote;
	}

	private static final int CORNER = 12;

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		int w = getRealSize().width;
		int h = getRealSize().height;
		drawer.drawLines(p(0, 0), p(w - CORNER, 0), p(w, CORNER), p(w, h), p(0, h), p(0, 0));
		drawer.drawLines(p(w - CORNER, 0), p(w - CORNER, CORNER), p(w, CORNER));
	}

	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}

}
