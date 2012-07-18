package com.umlet.element.experimental.uml;

import com.baselet.control.Constants.AlignHorizontal;
import com.umlet.element.experimental.Id;
import com.umlet.element.experimental.NewGridElement;



@Id("UMLClass")
public class Class extends NewGridElement {

	@Override
	public void updateModelFromText() {
		super.updateModelFromText();
		drawer.drawRectangle(0, 0, getRealSize().width-1, getRealSize().height-1);
		drawClassElements(getRealSize().width-1);
	}
	
	public void drawClassElements(int width) {
		AlignHorizontal align = AlignHorizontal.CENTER;
		float distanceBetweenTexts = drawer.textHeightWithSpace();
		float yPos = distanceBetweenTexts;
		for (String line : properties.getPropertiesTextFiltered()) {
			if (line.equals("--")) {
				align = AlignHorizontal.LEFT;
				float linePos = yPos - (distanceBetweenTexts/2);
				drawer.drawLine(0, linePos, width, linePos);
			}
			else {
				drawer.print(line, yPos, align);
			}
			yPos += distanceBetweenTexts;
		}
	}
}

