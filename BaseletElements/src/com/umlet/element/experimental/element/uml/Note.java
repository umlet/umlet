package com.umlet.element.experimental.element.uml;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsText;

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
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		int w = getRealSize().width-1;
		int h = getRealSize().height-1;
		drawer.drawLines(p(0, 0), p(w-CORNER, 0), p(w, CORNER), p(w, h), p(0, h), p(0, 0));
		drawer.drawLines(p(w-CORNER, 0), p(w-CORNER, CORNER), p(w, CORNER));
	}
	
	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}

}

