package com.baselet.element.facet;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.enums.Priority;
import com.baselet.gui.AutocompletionText;

/**
 * A Facet is a simple handler method which acts on certain lines and does a specific job if it should act.
 * It is important that Facets are ALWAYS STATELESS.
 * If any State is required, it should be stored using the {@link PropertiesParserState#getOrInitFacetResponse(Class, Object)} method
 */
public abstract class Facet {

	protected Logger log = LoggerFactory.getLogger(Facet.class);

	/**
	 * @param line the current line which is parsed
	 * @param state the current state of the parser
	 * @return true if the handleLine() method of this facet should be applied
	 */
	public abstract boolean checkStart(String line, PropertiesParserState state);

	/**
	 * This method is invoked at the time when a specific line is parsed
	 * @param line the current line which is parsed
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
	 * @param state the current state of the parser
	 * @param handledLines the list of lines this facet has been applied to (in the order of the handleLine calls)
	 */
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		// default is no action
	}

	/**
	 * facets with higher priority will be applied before facets with lower priority:
	 * The order is: For all lines
	 * 1. Check all First-Run Facets from HIGHEST ... LOWEST
	 * 2. Check all Second-Run Facets from HIGHEST ... LOWEST
	 */
	public Priority getPriority() {
		return Priority.DEFAULT;
	}

	/**
	 * The parser runs twice. Facets where this method returns true, are part of the first run, other facets are part of the second run
	 *
	 * Typically facets of the first run will influence the whole diagram, even if they are located at the bottom.
	 * e.g. bg=red must be known before drawing the common content of an element; style=autoresize must be known as soon as possible to make the size-calculations
	 *
	 * Facets of the second run have less side effects (e.g. printText just prints the current line, -- transforms to a horizontal line at the current print-position)
	 */
	public boolean handleOnFirstRun() {
		return false;
	}

}
