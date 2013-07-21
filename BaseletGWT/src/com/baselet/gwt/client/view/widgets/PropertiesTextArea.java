package com.baselet.gwt.client.view.widgets;

import com.baselet.element.HasPanelAttributes;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.google.gwt.uibinder.client.UiConstructor;

public class PropertiesTextArea extends MySuggestBox {
	
	private static final String DEFAULT_HELPTEXT = "Space for diagram notes";

	private OwnTextArea textArea;

	private MySuggestOracle oracle;

	private HasPanelAttributes gridElement;

	@UiConstructor
	public PropertiesTextArea() {
		this(new MySuggestOracle(), new OwnTextArea(), new DefaultSuggestionDisplay());
	}
	
	public PropertiesTextArea(final MySuggestOracle oracle, OwnTextArea textArea, final DefaultSuggestionDisplay display) {
		super(oracle, textArea, display);
		this.oracle = oracle;
		this.textArea = textArea;
	}
	
	public void setGridElement(HasPanelAttributes panelAttributeProvider) {
		this.gridElement = panelAttributeProvider;
		String panelAttributes = panelAttributeProvider.getPanelAttributes();
		if (panelAttributes == null) {
			panelAttributes = DEFAULT_HELPTEXT;
		}
		textArea.setValue(panelAttributes);
		oracle.setAutocompletionList(panelAttributeProvider.getAutocompletionList());
	}
	
	public void addInstantValueChangeHandler(InstantValueChangeHandler handler) {
		textArea.addInstantValueChangeHandler(handler);
	}
	
	/**
	 * also fire texthandlers if a text is inserted via choosing a selectionbox entry
	 */
	@Override
	public void setText(String text) {
		super.setText(text);
		textArea.fireHandlers();
	}

	public void updateElementOrHelptext() {
		if (gridElement != null) {
			gridElement.setPanelAttributes(getValue());
		}
	}
}
