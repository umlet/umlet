package com.baselet.client.copy.umlet.element.experimental.settings.facets;

import com.baselet.client.copy.diagram.draw.BaseDrawHandler;
import com.baselet.client.copy.gui.AutocompletionText;
import com.baselet.client.copy.umlet.element.experimental.PropertiesConfig;

public interface Facet {

	boolean checkStart(String line);
	
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig);
	
	boolean replacesText(String line);
	
	AutocompletionText[] getAutocompletionStrings();
}
