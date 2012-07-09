package com.umlet.element.experimental;

import com.baselet.control.Constants;
import com.baselet.control.Utils;



@Id("Test")
public class TestElement extends NewGridElement {

	@Override
	protected void paintElement() {
		drawer.drawRectangle(0, 0, getRealSize().width-1, getRealSize().height-1);

		boolean center = true;
		float distanceBetweenTexts = drawer.textHeight()+2;
		int yPos = (int) distanceBetweenTexts;
		for (String line : Utils.decomposeStrings(this.getPanelAttributes())) {
			if (line.equals("--")) {
				center = false;
				int linePos = (int) (yPos - (distanceBetweenTexts/2));
				drawer.drawLine(0, linePos, getRealSize().width-1, linePos);
			}
			else {
				if (center) drawer.printCenter(line, yPos);
				else drawer.printLeft(line, yPos);
			}
			yPos += (int) distanceBetweenTexts;
		}
	}

}

