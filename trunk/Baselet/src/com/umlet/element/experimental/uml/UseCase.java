package com.umlet.element.experimental.uml;

import com.baselet.control.Utils;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.Id;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;

@Id("UMLUseCase")
public class UseCase extends NewGridElement {

	@Override
	public void updateModelFromText() {
		super.updateModelFromText();
		drawClassElements(drawer, this.getPanelAttributes(), drawer.textHeight()+2, getRealSize().width-1);
	}
	
	public void drawClassElements(BaseDrawHandler drawer, String panelText, int lineHeight, int width) {
		AlignHorizontal align = AlignHorizontal.CENTER;
		float distanceBetweenTexts = lineHeight;
		int yPos = (int) distanceBetweenTexts;
		for (String line : properties.getPropertiesTextFiltered(panelText)) {
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

