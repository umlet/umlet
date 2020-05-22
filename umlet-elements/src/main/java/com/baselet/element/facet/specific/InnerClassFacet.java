package com.baselet.element.facet.specific;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.element.facet.Alignment;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class InnerClassFacet extends Facet {

	public static final InnerClassFacet INSTANCE = new InnerClassFacet();

	private InnerClassFacet() {}

	private static final int BUFFER_PIXEL_PER_INNER = 5;
	private static final int H_SPACE = 4;

	private static final String START = "{innerclass";
	private static final String END = "innerclass}";

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return line.equals(START) || line.equals(END);
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		Stack<ClassSettings> innerClassStartPoints = getOrInit(state);
		DrawHandler drawer = state.getDrawer();

		if (line.equals(START)) {
			ClassSettings settings = new ClassSettings(state.getAlignment().getHorizontal(), state.getAlignment().getVertical(), getDividerPos(drawer, state));
			innerClassStartPoints.add(settings);
			state.getBuffer().addToLeftAndRight(BUFFER_PIXEL_PER_INNER);
			state.increaseTextPrintPosition(H_SPACE);
			state.getAlignment().reset();
		}
		else if (line.equals(END)) {
			ClassSettings previousClassSettings = innerClassStartPoints.pop();
			double start = previousClassSettings.start;
			double height = getDividerPos(drawer, state) - start;
			XValues xLimit = state.getXLimits(height);

			ColorOwn oldColor = drawer.getBackgroundColor();
			drawer.setBackgroundColor(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.TRANSPARENT));
			drawer.drawRectangle(xLimit.getLeft(), start, xLimit.getSpace(), height);
			drawer.setBackgroundColor(oldColor);

			state.increaseTextPrintPosition(H_SPACE);
			state.getBuffer().addToLeftAndRight(-BUFFER_PIXEL_PER_INNER);
			Alignment alignment = state.getAlignment();
			alignment.setHorizontal(false, previousClassSettings.hAlign);
			alignment.setVertical(false, previousClassSettings.vAlign);
		}

	}

	private double getDividerPos(DrawHandler drawer, PropertiesParserState state) {
		return state.getTextPrintPosition() - drawer.textHeightMax();
	}

	private Stack<ClassSettings> getOrInit(PropertiesParserState state) {
		return state.getOrInitFacetResponse(InnerClassFacet.class, new Stack<ClassSettings>());
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
