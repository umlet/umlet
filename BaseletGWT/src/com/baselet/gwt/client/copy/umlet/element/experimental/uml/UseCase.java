package com.baselet.gwt.client.copy.umlet.element.experimental.uml;

import com.baselet.gwt.client.copy.element.StickingPolygon;
import com.baselet.gwt.client.copy.umlet.element.experimental.ElementId;
import com.baselet.gwt.client.copy.umlet.element.experimental.NewGridElement;
import com.baselet.gwt.client.copy.umlet.element.experimental.settings.Settings;
import com.baselet.gwt.client.copy.umlet.element.experimental.settings.SettingsUseCase;

public class UseCase extends NewGridElement {

	public static final ElementId ID = ElementId.UMLUseCase;
	@Override
	public ElementId getId() {
		return ID;
	}
	
	@Override
	public void updateConcreteModel() {
		drawer.drawEllipse(0, 0, getRealSize().width-1, getRealSize().height-1);
		properties.drawPropertiesText();
	}


	 @Override
	 public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		 //First point is the top left then the points are added clockwise
	 StickingPolygon p = new StickingPolygon();
	
	 p.addPoint(x + width / 4, y);
	 p.addPoint(x + width * 3 / 4, y);
				
	 p.addPoint(x + width, y + height / 4);
	 p.addPoint(x + width, y + height * 3 / 4);
				
	 p.addPoint(x + width * 3 / 4, y + height);
	 p.addPoint(x + width / 4, y + height);
				
	 p.addPoint(x, y + height * 3 / 4);
	 p.addPoint(x, y + height / 4, true);
				
	 return p;
	 }


	@Override
	public Settings getSettings() {
		return new SettingsUseCase();
	}
}

