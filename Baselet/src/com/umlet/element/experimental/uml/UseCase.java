package com.umlet.element.experimental.uml;

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
	
}

