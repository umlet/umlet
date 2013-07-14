package com.baselet.gwt.client.view.widgets;

import java.util.Collections;

import com.baselet.element.GridElement;
import com.baselet.gui.AutocompletionText;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

public class PropertiesTextArea extends MySuggestBox {
	
	private OwnTextArea textArea;

	private MySuggestOracle oracle;

	private GridElement gridElement;

	public PropertiesTextArea() {
		this(new MySuggestOracle(), new OwnTextArea(), new DefaultSuggestionDisplay());
	}
	
	public PropertiesTextArea(final MySuggestOracle oracle, OwnTextArea textArea, final DefaultSuggestionDisplay display) {
		super(oracle, textArea, display);
		this.oracle = oracle;
		this.textArea = textArea;
		
		this.addKeyDownHandler(new KeyDownHandler() { // CTRL+Space shows all suggestions
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
	
	public void setGridElement(GridElement gridElement) {
		this.gridElement = gridElement;
		textArea.setValue(gridElement.getPanelAttributes());
		oracle.setAutocompletionList(gridElement.getAutocompletionList());
	}
	
	public void setNoGridElement() {
		this.gridElement = null;
		textArea.setValue("");
		oracle.setAutocompletionList(Collections.<AutocompletionText>emptyList());
	}
	
	public GridElement getGridElement() {
		return gridElement;
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
}
