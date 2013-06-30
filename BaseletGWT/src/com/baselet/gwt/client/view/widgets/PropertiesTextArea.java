package com.baselet.gwt.client.view.widgets;

import java.util.ArrayList;
import java.util.List;

import com.baselet.element.GridElement;
import com.baselet.gui.AutocompletionText;
import com.baselet.gwt.client.KeyCodesExt;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class PropertiesTextArea extends MySuggestBox {
	
	private OwnTextArea textArea;

	private MySuggestOracle oracle;

	private GridElement gridElement;
	
	public PropertiesTextArea() {
		super(new MySuggestOracle(), new OwnTextArea());
		textArea = (OwnTextArea) getValueBox();
		oracle = (MySuggestOracle) getSuggestOracle();
		
		//TODO ctrl+space should show ALL suggestions
		
		this.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int code = event.getNativeKeyCode();
				if (event.isControlKeyDown() && KeyCodesExt.isSpace(code)) {
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
