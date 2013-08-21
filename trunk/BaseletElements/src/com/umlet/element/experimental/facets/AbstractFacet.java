package com.umlet.element.experimental.facets;

import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.Facet.Priority;

public abstract class AbstractFacet implements Facet {

	public abstract boolean checkStart(String line);

	public abstract void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig);

	public boolean replacesText(String line) {
		return true;
	}

	public abstract List<AutocompletionText> getAutocompletionStrings();

	public Priority getPriority() {
		return Priority.MEDIUM;
	}

}