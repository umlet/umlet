package com.baselet.element.facet.specific;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.customdrawings.DummyDrawHandler;
import com.baselet.element.settings.SettingsManualResizeTop;

public class TemplateClassFacetTest {

	private static final int ELEMENT_WIDTH = 200;
	private static final int ELEMENT_HEIGHT = 80;

	private RecordingDrawHandler drawHandler;
	private PropertiesParserState state;

	@Before
	public void before() {
		drawHandler = new RecordingDrawHandler();
		state = new PropertiesParserState(new SettingsManualResizeTop() {}, drawHandler);
		state.resetValues(new Dimension(ELEMENT_WIDTH, ELEMENT_HEIGHT), 0, true);
	}

	@Test
	public void noTemplateDrawsSimpleRect() {
		parse();
		assertEquals(Arrays.asList(rectToString(0, 0, ELEMENT_WIDTH, ELEMENT_HEIGHT, LineType.SOLID)), drawHandler.rectangleCalls);
		assertEquals(Collections.<String> emptyList(), drawHandler.printCalls);
	}

	@Test
	public void singleLineTemplate() {
		parse("template=0: Object");
		// box: width = 9 chars * 10 + 12 (LEFT_SPACE), height = 1 line * 10 + 3 (UPPER_SPACE) + 3 (LOWER_SPACE)
		assertEquals(Arrays.asList(rectToString(98, 0, 102, 16, LineType.DASHED)), drawHandler.rectangleCalls);
		assertEquals(Arrays.asList(textToString("0: Object", 195, 13, AlignHorizontal.RIGHT)), drawHandler.printCalls);
	}

	@Test
	public void multiLineTemplateSplitsAtLineEndChar() {
		parse("template=0: Object\\n1: Object");
		// box: width = widest line (9 chars * 10) + 12, height = 2 lines * 10 + 3 + 3
		assertEquals(Arrays.asList(rectToString(98, 0, 102, 26, LineType.DASHED)), drawHandler.rectangleCalls);
		assertEquals(Arrays.asList(
				textToString("0: Object", 195, 13, AlignHorizontal.RIGHT),
				textToString("1: Object", 195, 23, AlignHorizontal.RIGHT)),
				drawHandler.printCalls);
	}

	@Test
	public void escapedLineEndCharStaysOnOneLine() {
		parse("template=a\\\\nb");
		assertEquals(Arrays.asList(rectToString(138, 0, 62, 16, LineType.DASHED)), drawHandler.rectangleCalls);
		assertEquals(Arrays.asList(textToString("a\\\\nb", 195, 13, AlignHorizontal.RIGHT)), drawHandler.printCalls);
	}

	@Test
	public void templateRectWidthUsesWidestLine() {
		parse("template=ab\\ncdef");
		assertEquals(Arrays.asList(rectToString(148, 0, 52, 26, LineType.DASHED)), drawHandler.rectangleCalls);
		assertEquals(Arrays.asList(
				textToString("ab", 195, 13, AlignHorizontal.RIGHT),
				textToString("cdef", 195, 23, AlignHorizontal.RIGHT)),
				drawHandler.printCalls);
	}

	private void parse(String... handledLines) {
		TemplateClassFacet.INSTANCE.parsingFinished(state, Arrays.asList(handledLines));
	}

	private static String rectToString(double x, double y, double width, double height, LineType lineType) {
		return String.format("drawRectangle(%.3f, %.3f, %.3f, %.3f) lt=%s", x, y, width, height, lineType);
	}

	private static String textToString(String text, double x, double y, AlignHorizontal align) {
		return String.format("drawText('%s', %.3f, %.3f, %s)", text, x, y, align);
	}

	/**
	 * deterministic text metrics: every char is 10 wide, every line is 10 high
	 */
	private static class RecordingDrawHandler extends DummyDrawHandler {

		private final List<String> printCalls = new ArrayList<String>();
		private final List<String> rectangleCalls = new ArrayList<String>();

		@Override
		protected DimensionDouble textDimensionHelper(StringStyle singleLine) {
			return new DimensionDouble(singleLine.getStringWithoutMarkup().length() * 10, 10);
		}

		@Override
		public void print(String text, double x, double y, AlignHorizontal align) {
			printCalls.add(textToString(text, x, y, align));
		}

		@Override
		public void drawRectangle(double x, double y, double width, double height) {
			rectangleCalls.add(rectToString(x, y, width, height, getLineType()));
		}
	}
}
