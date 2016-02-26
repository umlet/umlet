package com.baselet.diagram.draw;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.element.facet.customdrawings.DummyDrawHandler;

public class TextSplitterTest {

	private static final DrawHandler dimensionCalculatorDrawHandler = new DummyDrawHandler() {
		@Override
		protected DimensionDouble textDimensionHelper(StringStyle sinlgeLine) {
			TextLayout tl = new TextLayout(new AttributedString(sinlgeLine.getStringWithoutMarkup()).getIterator(), new FontRenderContext(null, true, true));
			return new DimensionDouble(tl.getBounds().getWidth(), tl.getBounds().getHeight());
		}

	};

	@Test
	public void ifASingleWordDoesntFitTheSpaceSplitItIntoMultipleLines() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("MessagePresenter", 30.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Arrays.asList("Mes", "sag", "ePre", "sent", "er"));
	}

	@Test
	public void firstWordIsSplitPartiallyIntoSecondLineWithSecondWordThirdWordFitsLine() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("MessagePresenter (text) text3blaxxxx", 100.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Arrays.asList("MessagePresent", "er (text)", "text3blaxxxx"));
	}

	@Test
	public void shortWordThenLongWord() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("tter looooooooooooongword", 20.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Arrays.asList("tte", "r", "loo", "oo", "oo", "oo", "oo", "oo", "on", "gw", "or", "d"));
	}

	@Test
	public void severalWordsSplitIntoDistinctLines() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("test test2 test3 test4", 50.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Arrays.asList("test", "test2", "test3", "test4"));
	}

	@Test
	public void manyShortWordsWithEnoughSpace() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test", 5000.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Arrays.asList("test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test"));
	}

	@Test
	public void emptyResultIfNotEnoughSpaceForSingleChar() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 1.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Collections.<String> emptyList());
	}

	@Test
	public void emptyString() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("", 1.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Collections.<String> emptyList());
	}

	private void assertContentEquals(StringStyle[] style, List<String> expected) {
		assertThat(style.length).isEqualTo(expected.size());
		for (int i = 0; i < style.length; i++) {
			assertThat(style[i].getStringWithoutMarkup()).isEqualTo(expected.get(i));
		}
	}
}
