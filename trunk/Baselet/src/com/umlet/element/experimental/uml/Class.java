package com.umlet.element.experimental.uml;

import com.baselet.control.Constants.AlignHorizontal;
import com.umlet.element.experimental.Id;
import com.umlet.element.experimental.NewGridElement;



@Id("UMLClass")
public class Class extends NewGridElement {

	@Override
	public void updateConcreteModel() {
		drawer.drawRectangle(0, 0, getRealSize().width-1, getRealSize().height-1);
		properties.drawTextForClass();
	}
}

