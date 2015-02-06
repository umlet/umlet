package com.baselet.element.facet;

import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.gui.AutocompletionText;

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
	public abstract void handleLine(String line, DrawHandler drawer, PropertiesParserState state);

	/**
	 * @return a list of objects where each one represents one line for autocompletion
	 */
	public abstract List<AutocompletionText> getAutocompletionStrings();

	/**
	 * @param line the current line which is parsed
	 * @return true if the text line should be removed after applying this facet
	 */
	public boolean removeTextAfterHandling(String line) {
		return true;
	}

	/**
	 * This method is called once for every Facet AFTER all lines of text has been parsed
	 * E.g. useful for facets which collect information with every line but need complete knowledge before they can do something with it
	 *
	 * @param drawer can draw something on the elements space
	 * @param state the current state of the parser
	 */
	public void parsingFinished(DrawHandler drawer, PropertiesParserState state) {
		// default is no action
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
