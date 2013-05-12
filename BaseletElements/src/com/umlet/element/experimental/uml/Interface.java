package com.umlet.element.experimental.uml;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.element.StickingPolygon;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsClass;
import com.umlet.element.experimental.settings.SettingsInterface;

public class Interface extends NewGridElement {

	private int TOP_DISTANCE = 10;
	private int CIRCLE_SIZE = 20;

	@Override
	public ElementId getId() {
		return ElementId.UMLInterface;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		drawer.drawEllipse(getRealSize().getWidth() / 2 - CIRCLE_SIZE/2, TOP_DISTANCE, CIRCLE_SIZE, CIRCLE_SIZE);
		properties.drawPropertiesText();
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addPoint(getRealSize().getWidth() / 2 - CIRCLE_SIZE/2, TOP_DISTANCE);
		p.addPoint(getRealSize().getWidth() / 2 + CIRCLE_SIZE/2, TOP_DISTANCE);
		p.addPoint(getRealSize().getWidth() / 2 + CIRCLE_SIZE/2, TOP_DISTANCE+CIRCLE_SIZE);
		p.addPoint(getRealSize().getWidth() / 2 - CIRCLE_SIZE/2, TOP_DISTANCE+CIRCLE_SIZE, true);
		return p;
	}

	@Override
	public Settings getSettings() {
		return new SettingsInterface();
	}
}
