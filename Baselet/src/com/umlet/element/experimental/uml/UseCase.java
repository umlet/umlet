package com.umlet.element.experimental.uml;

import com.baselet.element.StickingPolygon;
import com.umlet.element.experimental.ElementFactory.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsUseCase;

public class UseCase extends NewGridElement {

	public static final ElementId ID = ElementId.UMLUseCase;
	@Override
	public ElementId getId() {
		return ID;
	}
	
	@Override
	public void updateConcreteModel() {
		int halfWidth = getRealSize().width/2;
		int halfHeight = getRealSize().height/2;
		drawer.drawEllipse(halfWidth, halfHeight, halfWidth-1, halfHeight-1);
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

