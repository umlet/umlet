package com.baselet.element.facet;

import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.enums.Priority;
import com.baselet.gui.AutocompletionText;

/**
 * A Facet is a simple handler method which acts on certain lines and does a specific job if it should act.
 * It is important that Facets are ALWAYS STATELESS.
 * If any State is required, it should be stored using the {@link PropertiesParserState#getOrInitFacetResponse(Class, Object)} method
 */
public abstract class Facet {

	public static final String SEP = "=";

	protected Logger log = Logger.getLogger(Facet.class);

	/**
	 * @param line the current line which is parsed
	 * @param state the current state of the parser
	 * @return true if the handleLine() method of this facet should be applied
	 */
	public abstract boolean checkStart(String line, PropertiesParserState state);

	/**
	 * @param line the current line which is parsed
	 * @param drawer can draw something on the elements space
	 * @param state the current state of the parser
	 */
	public abstract void handleLine(String line, PropertiesParserState state);

	/**
	 * @return a list of objects where each one represents one line for autocompletion
	 */
	public abstract List<AutocompletionText> getAutocompletionStrings();

	/**
	 * This method is called once for every Facet AFTER all lines of text has been parsed
	 * E.g. useful for facets which collect information with every line but need complete knowledge before they can do something with it
	 *
	 * @param drawer can draw something on the elements space
	 * @param state the current state of the parser
	 */
	public void parsingFinished(PropertiesParserState state) {
		// default is no action
	}

	/**
	 * facets with higher priority will be applied before facets with lower priority:
	 * The order is:
	 * 1. Check all Global Facets from HIGHEST ... LOWEST for all lines
	 * 2. Check all other Facets from HIGHEST ... LOWEST
	 */
	public Priority getPriority() {
		return Priority.DEFAULT;
	}

}
