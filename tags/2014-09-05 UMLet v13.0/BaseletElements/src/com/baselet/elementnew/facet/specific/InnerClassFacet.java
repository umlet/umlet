package com.baselet.elementnew.facet.specific;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.gui.AutocompletionText;

public class InnerClassFacet extends Facet {

	public static InnerClassFacet INSTANCE = new InnerClassFacet();

	private InnerClassFacet() {}

	private static final int BUFFER_PIXEL_PER_INNER = 5;
	private static final int H_SPACE = 4;

	private static final String START = "{innerclass";
	private static final String END = "innerclass}";

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return line.equals(START) || !getOrInit(state).isEmpty();
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		Stack<ClassSettings> innerClassStartPoints = getOrInit(state);

		if (line.equals(START)) {
			ClassSettings settings = new ClassSettings(state.gethAlign(), state.getvAlign(), getDividerPos(drawer, state));
			innerClassStartPoints.add(settings);
			state.addToHorizontalBuffer(BUFFER_PIXEL_PER_INNER);
			state.addToYPos(H_SPACE);
			state.resetAlign();
		}
		else if (line.equals(END)) {
			ClassSettings previousClassSettings = innerClassStartPoints.pop();
			double start = previousClassSettings.start;
			double height = getDividerPos(drawer, state) - start;
			XValues xLimit = state.getXLimits(height);

			ColorOwn oldColor = drawer.getStyle().getBackgroundColor();
			drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
			drawer.drawRectangle(xLimit.getLeft(), start, xLimit.getSpace(), height);
			drawer.setBackgroundColor(oldColor);

			state.addToYPos(H_SPACE);
			state.addToHorizontalBuffer(-BUFFER_PIXEL_PER_INNER);
			state.sethAlign(previousClassSettings.hAlign);
			state.setvAlign(previousClassSettings.vAlign);
		}

	}

	private double getDividerPos(DrawHandler drawer, PropertiesParserState state) {
		return state.getyPos() - drawer.textHeightMax();
	}

	private Stack<ClassSettings> getOrInit(PropertiesParserState state) {
		return state.getOrInitFacetResponse(InnerClassFacet.class, new Stack<ClassSettings>());
	}

	@Override
	public boolean replacesText(String line) {
		return line.equals(START) || line.equals(END); // only these 2 lines should not be printed
	}

	private static class ClassSettings {
		private final AlignHorizontal hAlign;
		private final AlignVertical vAlign;
		private final double start;

		public ClassSettings(AlignHorizontal hAlign, AlignVertical vAlign, double startpoint) {
			super();
			this.hAlign = hAlign;
			this.vAlign = vAlign;
			start = startpoint;
		}

	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(START, "begin inner class"), new AutocompletionText(END, "end inner class"));
	}

}
