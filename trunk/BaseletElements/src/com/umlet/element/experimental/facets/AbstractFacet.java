package com.umlet.element.experimental.facets;

import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.gui.AutocompletionText;

public abstract class AbstractFacet implements Facet {
	
	protected Logger log = Logger.getLogger(AbstractFacet.class);

	public boolean replacesText(String line) {
		return true;
	}

	public abstract List<AutocompletionText> getAutocompletionStrings();

	public Priority getPriority() {
		return Priority.MEDIUM;
	}

}