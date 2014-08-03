package com.baselet.elementnew.facet.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.GlobalFacet;
import com.baselet.gui.AutocompletionText;

public class TextBeforeFirstSeparatorCollectorFacet extends GlobalFacet {

	public static TextBeforeFirstSeparatorCollectorFacet INSTANCE = new TextBeforeFirstSeparatorCollectorFacet();

	protected TextBeforeFirstSeparatorCollectorFacet() {}

	public static class PackageTitleFacetResponse {
		private boolean firstSepFound = false;
		private final List<String> lines = new ArrayList<String>();

		public List<String> getLines() {
			return lines;
		}
	}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return getOrInit(state).firstSepFound == false;
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		if (line.equals(SeparatorLineFacet.KEY)) {
			getOrInit(state).firstSepFound = true;
			return;
		}
		else {
			getOrInit(state).getLines().add(line);
		}
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(SeparatorLineFacet.KEY, "ends package title part"));
	}

	@Override
	public Priority getPriority() {
		return Priority.LOWEST; // the collector should only collect lines which are not parsed by any other facet
	}

	private PackageTitleFacetResponse getOrInit(PropertiesParserState state) {
		return state.getOrInitFacetResponse(TextBeforeFirstSeparatorCollectorFacet.class, new PackageTitleFacetResponse());
	}

}
