package com.baselet.element.facet.customdrawings;

import java.util.List;

import org.junit.Test;

import com.baselet.gui.AutocompletionText;

/**
 * Tests if all code completion suggestions except the start symbol are parseable.
 * This ensures that no error is displayed when selecting a suggestion.
 */
public class AutocompletionValid {

	@Test
	public void testAutocompletionSuggestions() {
		List<AutocompletionText> suggestions = new CustomDrawingFacet().getAutocompletionStrings();
		for (AutocompletionText t : suggestions) {
			if (!CustomDrawingFacet.CODE_SEP_START.equals(t.getText())) {
				new CustomDrawingParserImpl(t.getText(), 0, 0, new DummyDrawHandler()).parse();
			}
		}

	}
}
