package com.baselet.element.facet.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class FontFamilyFacet extends FirstRunKeyValueFacet {

	public static final FontFamilyFacet INSTANCE = new FontFamilyFacet();
	private static final List<String> FAMILIES = Arrays.asList("SansSerif", "Serif", "Monospaced");

	private FontFamilyFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("fontfamily", false, "SansSerif", "logical font family: SansSerif, Serif or Monospaced (Swing only)");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		// Accept the trailing comment in the diagram's documented fontfamily example.
		String family = value.split("//", 2)[0].trim();
		if (!FAMILIES.contains(family)) {
			throw new StyleException("fontfamily must be SansSerif, Serif or Monospaced");
		}
		state.getDrawer().setFontFamily(family);
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		List<AutocompletionText> result = new ArrayList<AutocompletionText>();
		for (String family : FAMILIES) {
			result.add(new AutocompletionText("fontfamily=" + family, "element font family (Swing only)"));
		}
		return result;
	}
}
