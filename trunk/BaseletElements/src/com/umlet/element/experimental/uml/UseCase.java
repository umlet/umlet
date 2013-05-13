package com.umlet.element.experimental.uml;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.element.StickingPolygon;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsUseCase;

public class UseCase extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLUseCase;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		drawer.drawEllipse(0, 0, getRealSize().width-1, getRealSize().height-1);
		properties.drawPropertiesText();
	}


	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon(x, y);

		p.addPoint(width / 4, 0);
		p.addPoint(width * 3 / 4, 0);

		p.addPoint(width, height / 4);
		p.addPoint(width, height * 3 / 4);

		p.addPoint(width * 3 / 4, height);
		p.addPoint(width / 4, height);

		p.addPoint(0, height * 3 / 4);
		p.addPoint(0, height / 4, true);
		
		return p;
	}


	@Override
	public Settings getSettings() {
		return new SettingsUseCase();
	}
}

