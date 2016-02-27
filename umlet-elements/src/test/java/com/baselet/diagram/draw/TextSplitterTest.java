package com.baselet.diagram.draw;

import static org.assertj.core.api.Assertions.assertThat;

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
			// return dimensions which do not depend on local swing or font settings to make sure tests work on every JRE
			return new DimensionDouble(sinlgeLine.getStringWithoutMarkup().length() * 7, 10);
		}

	};

	@Test
	public void ifASingleWordDoesntFitTheSpaceSplitItIntoMultipleLines() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("MessagePresenter", 30.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Arrays.asList("Mes", "sag", "ePr", "ese", "nte", "r"));
	}

	@Test
	public void firstWordIsSplitPartiallyIntoSecondLineWithSecondWordThirdWordFitsLine() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("MessagePresenter (text) text3blaxxxx", 100.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Arrays.asList("MessagePresen", "ter (text)", "text3blaxxxx"));
	}

	@Test
	public void shortWordThenLongWord() throws Exception {
		StringStyle[] style = TextSplitter.splitStringAlgorithm("tter looooooooooooongword", 30.0, dimensionCalculatorDrawHandler);
		assertContentEquals(style, Arrays.asList("tte", "r", "loo", "ooo", "ooo", "ooo", "oon", "gwo", "rd"));
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
