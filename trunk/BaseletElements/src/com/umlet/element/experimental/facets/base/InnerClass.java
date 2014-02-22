package com.umlet.element.experimental.facets.base;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractFacet;

public class InnerClass extends AbstractFacet {

	private static final int BUFFER_PIXEL_PER_INNER = 5;
	private static final int H_SPACE = 4;

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
			ClassSettings settings = new ClassSettings(propConfig.gethAlign(), propConfig.getvAlign(), propConfig.getDividerPos(drawer));
			innerClassStartPoints.add(settings);
			propConfig.addToBuffer(innerClassStartPoints.size() * BUFFER_PIXEL_PER_INNER);
			propConfig.addToYPos(H_SPACE);
			propConfig.resetAlign();
		}
		else if (line.equals(END)) {
			int depth = innerClassStartPoints.size() * BUFFER_PIXEL_PER_INNER;
			ClassSettings previousClassSettings = innerClassStartPoints.pop();
			double start = previousClassSettings.start;
			double height = propConfig.getDividerPos(drawer) - start;
			XValues xLimit = propConfig.getXLimits(height);
			
			drawer.drawRectangle(xLimit.getLeft(), start, xLimit.getSpace(), height);
			
			propConfig.addToYPos(H_SPACE);
			propConfig.addToBuffer(-depth);
			propConfig.sethAlign(previousClassSettings.hAlign);
			propConfig.setvAlign(previousClassSettings.vAlign);
		}

	}

	@Override
	public boolean replacesText(String line) {
		return line.equals(START) || line.equals(END); //only these 2 lines should not be printed
	}

	private static class ClassSettings {
		private AlignHorizontal hAlign;
		private AlignVertical vAlign;
		private double start;

		public ClassSettings(AlignHorizontal hAlign, AlignVertical vAlign, double startpoint) {
			super();
			this.hAlign = hAlign;
			this.vAlign = vAlign;
			this.start = startpoint;
		}

	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(START, "begin inner class"), new AutocompletionText(END, "end inner class"));
	}

}
