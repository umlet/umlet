package com.umlet.element.experimental.facets;

import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public interface Facet {

	public static final String SEP = "=";
	
	/**
	 * priority enum, must be ordered from highest to lowest priority!
	 */
	public enum Priority {HIGH, MEDIUM}
	
	boolean checkStart(String line);
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig);
	boolean replacesText(String line);
	List<AutocompletionText> getAutocompletionStrings();

	/**
	 * facets with higher priority will be applied before facets with lower priority
	 */
	Priority getPriority();
}
