package com.umlet.element.experimental.settings.facets;

import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public interface Facet {

	boolean checkStart(String line);
	
	void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig);
	
	boolean replacesText(String line);
	
	AutocompletionText[] getAutocompletionStrings();
}
