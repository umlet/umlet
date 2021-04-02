package com.baselet.gwt.client.view.widgets.propertiespanel;

import com.baselet.element.interfaces.HasPanelAttributes;
import com.baselet.gui.AutocompletionText;
import com.baselet.gwt.client.view.widgets.OwnTextArea;
import com.google.gwt.uibinder.client.UiConstructor;

import java.util.ArrayList;
import java.util.List;

public class CustomDrawingsTextArea extends TextArea {

	@UiConstructor
	public CustomDrawingsTextArea() {
		super();
	}

	public CustomDrawingsTextArea(final MySuggestOracle oracle, OwnTextArea textArea) {
		super(oracle, textArea);
	}

	/**
	 * also fire texthandlers if a text is inserted via choosing a selectionbox entry
	 */
	@Override
	public void setText(String text) {
		super.setText(text);
		textArea.fireHandler();
	}

	@Override
	public void setAutocompletionList(HasPanelAttributes panelAttributeProvider) {
		List<AutocompletionTextGwt> autocompletionTextList = new ArrayList<>();
		for (AutocompletionText oldText : panelAttributeProvider.getCustomDrawingsAutocompletionList()) {
			AutocompletionTextGwt newText = new AutocompletionTextGwt(oldText.getText(), oldText.getInfo(), oldText.getBase64Img());
			autocompletionTextList.add(newText);
		}
		oracle.setAutocompletionList(autocompletionTextList);
	}

	@Override
	public String getGridElementAttributes(HasPanelAttributes panelAttributeProvider) {
		return panelAttributeProvider.getCustomDrawingsCode();
	}

	@Override
	public void setGridElementAttributes() {
		gridElement.setCustomDrawingsCode(getValue());
	}
}
