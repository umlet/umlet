package com.umlet.element.experimental.uml;

import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
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
		drawClassElements(getRealSize().width, getRealSize().height, AlignVertical.TOP);
	}
	
	public void drawClassElements(int width, int height, AlignVertical verticalAlign) {
		Vector<String> propertiesTextFiltered = properties.getPropertiesTextFiltered();
		float distanceBetweenTexts = drawer.textHeightWithSpace();
		float yPos = calcStartPos(propertiesTextFiltered.get(0), width, height, propertiesTextFiltered.size()*distanceBetweenTexts, verticalAlign);

		for (String line : propertiesTextFiltered) {
			if (line.equals("--")) {
				float linePos = yPos - (distanceBetweenTexts/2);
				float[] xPos = getXValuesOnEllipse(linePos, height, width);
				drawer.drawLine(xPos[0]+1, linePos, xPos[1]-1, linePos);
			}
			else {
				drawer.print(line, (int) yPos, AlignHorizontal.CENTER);
			}
			yPos += distanceBetweenTexts;
		}
	}
	
	private float calcStartPos(String firstLine, int width, int height, float textBlockHeight, AlignVertical verticalAlign) {
		if (verticalAlign == AlignVertical.TOP) return calcNotInterferingStartPoint(firstLine, width, height, drawer.textHeight()/2, -drawer.textHeight(), drawer.textHeightWithSpace());
		else if (verticalAlign == AlignVertical.CENTER) return Math.max((this.getRealSize().height - textBlockHeight)/2, drawer.textHeightWithSpace());
		else /*if (verticalAlign == AlignVertical.BOTTOM)*/ return Math.max(this.getRealSize().height - textBlockHeight, drawer.textHeightWithSpace());
	}

	private float calcNotInterferingStartPoint(String firstLine, int width, int height, float increment, float relevantDisplacement, float start) {
		float yPos = start;
		float spaceNeededForText = drawer.textWidth(firstLine);
		float[] xVals;
		float availableSpace;
		do {
			yPos += increment;
			xVals = getXValuesOnEllipse(yPos+relevantDisplacement, height, width);
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

