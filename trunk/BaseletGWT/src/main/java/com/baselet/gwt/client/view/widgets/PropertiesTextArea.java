package com.baselet.gwt.client.view.widgets;

import com.baselet.element.HasPanelAttributes;
import com.baselet.gwt.client.view.DrawPanel;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.google.gwt.uibinder.client.UiConstructor;

public class PropertiesTextArea extends MySuggestBox {
	
	private static final String DEFAULT_HELPTEXT = "Space for diagram notes";

	private OwnTextArea textArea;

	private MySuggestOracle oracle;

	private HasPanelAttributes gridElement;
	
	private DrawPanel activePanel = null;

	@UiConstructor
	public PropertiesTextArea() {
		this(new MySuggestOracle(), new OwnTextArea(), new DefaultSuggestionDisplay());

		textArea.setInstantValueChangeHandler(new InstantValueChangeHandler() {
			@Override
			public void onValueChange(String value) {
				if (gridElement != null) {
					gridElement.setPanelAttributes(getValue());
				}
				activePanel.redraw();
			}
		});
	}
	
	public PropertiesTextArea(final MySuggestOracle oracle, OwnTextArea textArea, final DefaultSuggestionDisplay display) {
		super(oracle, textArea, display);
		this.oracle = oracle;
		this.textArea = textArea;
	}
	
	public void setGridElement(HasPanelAttributes panelAttributeProvider, DrawPanel panel) {
		activePanel = panel;
		this.gridElement = panelAttributeProvider;
		String panelAttributes = panelAttributeProvider.getPanelAttributes();
		if (panelAttributes == null) {
			panelAttributes = DEFAULT_HELPTEXT;
		}
		textArea.setValue(panelAttributes);
		oracle.setAutocompletionList(panelAttributeProvider.getAutocompletionList());
	}
	
	/**
	 * also fire texthandlers if a text is inserted via choosing a selectionbox entry
	 */
	@Override
	public void setText(String text) {
		super.setText(text);
		textArea.fireHandler();
	}
}
