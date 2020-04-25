package com.baselet.element.facet.common;

import java.util.Collections;
import java.util.List;

import com.baselet.control.enums.Priority;
import com.baselet.element.facet.FirstRunFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class CommentFacet extends FirstRunFacet {

	public static final CommentFacet INSTANCE = new CommentFacet();

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return line.startsWith("//"); // comments start with // and are removed
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		// do nothing, just remove the line
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Collections.<AutocompletionText> emptyList();
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
