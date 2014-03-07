package com.umlet.element.experimental.facet;

import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public abstract class Facet {
	
	public static final String SEP = "=";
	
	protected Logger log = Logger.getLogger(Facet.class);

	public abstract boolean checkStart(String line, PropertiesConfig propConfig);
	public abstract void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig);
	public abstract List<AutocompletionText> getAutocompletionStrings();

	public boolean replacesText(String line) {
		return true;
	}
	
	/**
	 * priority enum, must be ordered from highest to lowest priority!
	 */
	public enum Priority {HIGHER, HIGH, MEDIUM}
	
	/**
	 * facets with higher priority will be applied before facets with lower priority
	 */
	public Priority getPriority() {
		return Priority.MEDIUM;
	}

}
