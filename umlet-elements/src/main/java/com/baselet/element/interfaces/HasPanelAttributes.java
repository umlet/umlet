package com.baselet.element.interfaces;

import java.util.List;

import com.baselet.gui.AutocompletionText;

public interface HasPanelAttributes {

	String getPanelAttributes();

	void setPanelAttributes(String panelAttributes);

	String getCustomDrawingsCode();

	void setCustomDrawingsCode(String customDrawingsCode);

	List<AutocompletionText> getAutocompletionList();

}
