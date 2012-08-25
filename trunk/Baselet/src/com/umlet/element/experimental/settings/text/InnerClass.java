package com.umlet.element.experimental.settings.text;

import java.util.Stack;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.helper.XPoints;

public class InnerClass implements Facet {

	private static final int BUFFER_PIXEL_PER_INNER = 5;

	private Stack<ClassSettings> innerClassStartPoints = new Stack<ClassSettings>();

	private static final String START = "{innerclass";
	private static final String END = "innerclass}";

	@Override
	public boolean checkStart(String line) {
		return line.equals(START) || !innerClassStartPoints.isEmpty();
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		if (line.equals(START)) {
			ClassSettings settings = new ClassSettings(propConfig.gethAlign(), propConfig.getvAlign(), Helper.getHLinePos(drawer, propConfig));
			innerClassStartPoints.add(settings);
			propConfig.addToBuffer(innerClassStartPoints.size() * BUFFER_PIXEL_PER_INNER);
			Helper.drawHorizontalLine(getHorizontalSpace(line), drawer, propConfig);
			propConfig.resetAlign();
		}
		else if (line.equals(END)) {
			int depth = innerClassStartPoints.size() * BUFFER_PIXEL_PER_INNER;
			ClassSettings previousClassSettings = innerClassStartPoints.pop();
			float end = Helper.getHLinePos(drawer, propConfig);
			XPoints xLimit = propConfig.getXLimits(end);
			drawer.drawLine(xLimit.getLeft(), previousClassSettings.start, xLimit.getLeft(), end);
			drawer.drawLine(xLimit.getRight(), previousClassSettings.start, xLimit.getRight(), end);
			Helper.drawHorizontalLine(getHorizontalSpace(line), drawer, propConfig);
			propConfig.addToBuffer(-depth);
			propConfig.sethAlign(previousClassSettings.hAlign);
			propConfig.setvAlign(previousClassSettings.vAlign);
		}

	}

	@Override
	public float getHorizontalSpace(String line) {
		if (replacesText(line)) {
			return 4; // is equal to separatorline
		}
		else return 0;
	}

	@Override
	public boolean replacesText(String line) {
		return line.equals(START) || line.equals(END); //only these 2 lines should not be printed
	}

	private static class ClassSettings {
		private AlignHorizontal hAlign;
		private AlignVertical vAlign;
		private float start;

		public ClassSettings(AlignHorizontal hAlign, AlignVertical vAlign, float startpoint) {
			super();
			this.hAlign = hAlign;
			this.vAlign = vAlign;
			this.start = startpoint;
		}

	}

}
