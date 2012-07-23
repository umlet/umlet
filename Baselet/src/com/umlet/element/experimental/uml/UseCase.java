package com.umlet.element.experimental.uml;

import com.baselet.element.StickingPolygon;
import com.umlet.element.experimental.Id;
import com.umlet.element.experimental.NewGridElement;

@Id("UMLUseCase")
public class UseCase extends NewGridElement {

	@Override
	public void updateModelFromText() {
		super.updateModelFromText();
		int halfWidth = getRealSize().width/2;
		int halfHeight = getRealSize().height/2;
		drawer.drawEllipse(halfWidth, halfHeight, halfWidth-1, halfHeight-1);
		properties.drawTextForUseCase(getRealSize().width, getRealSize().height);
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
}

