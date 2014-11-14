package com.baselet.elementnew.element.uml;

import com.baselet.control.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.base.ElementId;
import com.baselet.elementnew.facet.PropertiesParserState;
import com.baselet.elementnew.facet.Settings;
import com.baselet.elementnew.settings.SettingsText;

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
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		int w = getRealSize().width;
		int h = getRealSize().height;
		drawer.drawLines(p(0, 0), p(w - CORNER, 0), p(w, CORNER), p(w, h), p(0, h), p(0, 0));
		drawer.drawLines(p(w - CORNER, 0), p(w - CORNER, CORNER), p(w, CORNER));
	}

	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}

}
