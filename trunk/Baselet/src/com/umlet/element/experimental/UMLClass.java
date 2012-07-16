package com.umlet.element.experimental;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.diagram.draw.BaseDrawHandler;



@Id("Class")
public class UMLClass extends NewGridElement {

	@Override
	public void updateModelFromText() {
		super.updateModelFromText();
		drawer.drawRectangle(0, 0, getRealSize().width-1, getRealSize().height-1);
		drawClassElements(drawer, this.getPanelAttributes(), drawer.textHeight()+2, getRealSize().width-1);
	}
	
	public static void drawClassElements(BaseDrawHandler drawer, String panelText, int lineHeight, int width) {
		AlignHorizontal align = AlignHorizontal.CENTER;
		float distanceBetweenTexts = lineHeight;
		int yPos = (int) distanceBetweenTexts;
		for (String line : Utils.decomposeStrings(panelText)) {
			if (line.equals("--")) {
				align = AlignHorizontal.LEFT;
				int linePos = (int) (yPos - (distanceBetweenTexts/2));
				drawer.drawLine(0, linePos, width, linePos);
			}
			else {
				drawer.print(line, yPos, align);
			}
			yPos += (int) distanceBetweenTexts;
		}
	}
}

