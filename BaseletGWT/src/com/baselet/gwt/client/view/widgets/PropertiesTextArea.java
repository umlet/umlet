package com.baselet.gwt.client.view.widgets;

import com.baselet.element.HasPanelAttributes;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

public class PropertiesTextArea extends MySuggestBox {
	
	private static final String DEFAULT_HELPTEXT = "Space for diagram notes";

	private OwnTextArea textArea;

	private MySuggestOracle oracle;

	private HasPanelAttributes gridElement;

	public PropertiesTextArea() {
		this(new MySuggestOracle(), new OwnTextArea(), new DefaultSuggestionDisplay());
	}
	
	public PropertiesTextArea(final MySuggestOracle oracle, OwnTextArea textArea, final DefaultSuggestionDisplay display) {
		super(oracle, textArea, display);
		this.oracle = oracle;
		this.textArea = textArea;
		
		textArea.addKeyDownHandler(new KeyDownHandler() { // CTRL+Space shows all suggestions
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (Shortcut.SHOW_AUTOCOMPLETION.matches(event)) {
					oracle.setShowAllAsDefault(true);
					showSuggestionList();
					oracle.setShowAllAsDefault(false);
				}
			}
		});
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
