package com.umlet.element.experimental.uml;

import java.util.Vector;

import com.umlet.element.experimental.Id;
import com.umlet.element.experimental.NewGridElement;

@Id("UMLUseCase")
public class UseCase extends NewGridElement {

	@Override
	public void updateModelFromText() {
		super.updateModelFromText();
		int halfWidth = getSize().width/2;
		int halfHeight = getSize().height/2;
		drawer.drawEllipse(halfWidth, halfHeight, halfWidth-1, halfHeight-1);
		drawClassElements(getSize().height/50, getRealSize().width-1);
	}

	public void drawClassElements(int emptyUpperArea, int width) {
		int a = Math.max(1, (getSize().width - 1) / 2);
		int b = (getSize().height - 1) / 2;
		int x = ((getSize().width - 1) / 9 * 4);
		int y = (int) Math.round((Math.sqrt(((a * a * b * b) - (b * b * x * x)) / (a * a))));
		int yLinePos = b - y;

		Vector<String> propertiesTextFiltered = properties.getPropertiesTextFiltered();
		
		float distanceBetweenTexts = drawer.textHeightWithSpace();
		int yPos;
		if (propertiesTextFiltered.contains("--")) {
			yPos = (int) (distanceBetweenTexts/2 + yLinePos/2);
		} else {
			yPos = (int) Math.max((this.getSize().height - propertiesTextFiltered.size()*distanceBetweenTexts)/2, distanceBetweenTexts);
		}
		for (String line : propertiesTextFiltered) {
			if (line.equals("--")) {
				drawer.drawLine(a - x, yLinePos, a + x, yLinePos);
				yPos = (int) (yLinePos + distanceBetweenTexts/2);
			}
			else {
				drawer.printCenter(line, yPos);
			}
			yPos += (int) distanceBetweenTexts;
		}
	}
}

