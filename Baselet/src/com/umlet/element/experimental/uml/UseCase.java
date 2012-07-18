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
		double distanceBetweenTexts = drawer.textHeightWithSpace();
		double yPos = calcStartPos(propertiesTextFiltered.get(0), width, height);
		for (String line : propertiesTextFiltered) {
			if (line.equals("--")) {
				double linePos = yPos - (distanceBetweenTexts/2);
				double[] xPos = getXValuesOnEllipse(linePos, height, width);
				drawer.drawLine(xPos[0], linePos, xPos[1], linePos);
			}
			else {
				drawer.print(line, (int) yPos, AlignHorizontal.CENTER);
			}
			yPos += distanceBetweenTexts;
		}
	}
	
	private double calcStartPos(String firstLine, int width, int height) {
		double yPos = drawer.textHeightWithSpace();
		double spaceNeededForText = drawer.textWidth(firstLine);
		double[] xVals;
		double availableSpace;
		do {
			yPos += 5;
			xVals = getXValuesOnEllipse(yPos-drawer.textHeight(), height, width);
			availableSpace = xVals[1]-xVals[0];
		} while (availableSpace <= spaceNeededForText && yPos < height/2);
		return yPos;
	}

	/**
	 * calculates the left and right x value for a certain y value on an ellipse
	 */
	public double[] getXValuesOnEllipse(double y, int height, int width) {
		double b = height/2;
		double a = width/2;
		int x = (int) Math.sqrt((1-(Math.pow(b-y, 2) / Math.pow(b, 2)))*Math.pow(a, 2));
		return new double[] {a-x, a+x};
	}
}

