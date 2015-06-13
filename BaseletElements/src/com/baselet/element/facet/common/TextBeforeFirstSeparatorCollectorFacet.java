package com.baselet.element.facet.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.enums.Priority;
import com.baselet.element.facet.FirstRunFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

/**
 * the collector is the last first-run-facet which should be applied (therefore LOW prio)
 * it must be in first-run to be applied before the drawCommonContent of the GridElement but after any other first-run-facet
 */
public class TextBeforeFirstSeparatorCollectorFacet extends FirstRunFacet {

	private static final String KEY = "--";

	public static final TextBeforeFirstSeparatorCollectorFacet INSTANCE = new TextBeforeFirstSeparatorCollectorFacet();

	protected TextBeforeFirstSeparatorCollectorFacet() {}

	public static class TextBeforeFirstSeparatorCollectorFacetResponse {
		private boolean firstSepFound = false;
		private final List<String> lines = new ArrayList<String>();

		public List<String> getLines() {
			return lines;
		}
	}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return !getOrInit(state).firstSepFound;
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		if (line.equals(KEY)) {
			getOrInit(state).firstSepFound = true;
			return;
		}
		else {
			getOrInit(state).getLines().add(line);
		}
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(KEY, "end package title part (only first occurrence)"));
	}

	@Override
	public Priority getPriority() {
		return Priority.LOW; // only collect which is not used by any other facet
	}

	private TextBeforeFirstSeparatorCollectorFacetResponse getOrInit(PropertiesParserState state) {
		return state.getOrInitFacetResponse(TextBeforeFirstSeparatorCollectorFacet.class, new TextBeforeFirstSeparatorCollectorFacetResponse());
	}

}
