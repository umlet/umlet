package com.umlet.element.experimental.uml;

import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
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
		drawClassElements(getRealSize().width, getRealSize().height);
	}
	
	public void drawClassElements(int width, int height) {
		Vector<String> propertiesTextFiltered = properties.getPropertiesTextFiltered();
		float distanceBetweenTexts = drawer.textHeightWithSpace();
		float yPos = calcStartPos(propertiesTextFiltered.get(0), width, height);
		for (String line : propertiesTextFiltered) {
			if (line.equals("--")) {
				float linePos = yPos - (distanceBetweenTexts/2);
				float[] xPos = getXValuesOnEllipse(linePos, height, width);
				drawer.drawLine(xPos[0], linePos, xPos[1], linePos);
			}
			else {
				drawer.print(line, (int) yPos, AlignHorizontal.CENTER);
			}
			yPos += distanceBetweenTexts;
		}
	}
	
	private float calcStartPos(String firstLine, int width, int height) {
		float yPos = drawer.textHeightWithSpace();
		float spaceNeededForText = drawer.textWidth(firstLine);
		float[] xVals;
		float availableSpace;
		do {
			yPos += drawer.textHeight()/2;
			xVals = getXValuesOnEllipse(yPos-drawer.textHeight(), height, width);
			availableSpace = xVals[1]-xVals[0];
		} while (availableSpace <= spaceNeededForText && yPos < height/2);
		return yPos;
	}

	/**
	 * calculates the left and right x value for a certain y value on an ellipse
	 */
	public float[] getXValuesOnEllipse(float y, int height, int width) {
		float b = height/2;
		float a = width/2;
		int x = (int) Math.sqrt((1-(Math.pow(b-y, 2) / Math.pow(b, 2)))*Math.pow(a, 2));
		return new float[] {a-x, a+x};
	}
}

