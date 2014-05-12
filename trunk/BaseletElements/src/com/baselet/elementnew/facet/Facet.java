package com.baselet.elementnew.facet;

import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public abstract class Facet {

	public static final String SEP = "=";

	protected Logger log = Logger.getLogger(Facet.class);

	public abstract boolean checkStart(String line, PropertiesParserState state);

	public abstract void handleLine(String line, DrawHandler drawer, PropertiesParserState state);

	public abstract List<AutocompletionText> getAutocompletionStrings();

	public boolean replacesText(@SuppressWarnings("unused") String line) {
		return true;
	}

	/**
	 * priority enum, must be ordered from highest to lowest priority!
	 */
	public enum Priority {
		HIGHEST, HIGH, DEFAULT, LOWER, LOWEST
	}

	/**
	 * facets with higher priority will be applied before facets with lower priority
	 */
	public Priority getPriority() {
		return Priority.DEFAULT;
	}

}
