package com.baselet.gwt.client.view.widgets.propertiespanel;

import com.baselet.element.interfaces.HasPanelAttributes;
import com.baselet.gwt.client.view.DrawPanelDiagram;
import com.baselet.gwt.client.view.interfaces.Redrawable;
import com.baselet.gwt.client.view.widgets.OwnTextArea;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.google.gwt.uibinder.client.UiConstructor;

public class PropertiesTextArea extends MySuggestBox {

	private static final String DEFAULT_HELPTEXT = "Space for diagram notes";

	private final OwnTextArea textArea;

	private final MySuggestOracle oracle;

	private HasPanelAttributes gridElement;

	private Redrawable activePanel = null;

	@UiConstructor
	public PropertiesTextArea() {
		this(new MySuggestOracle(), new OwnTextArea());

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

	public PropertiesTextArea(final MySuggestOracle oracle, OwnTextArea textArea) {
		super(oracle, textArea);
		this.oracle = oracle;
		this.textArea = textArea;
	}
	



	public void setGridElement(HasPanelAttributes panelAttributeProvider, Redrawable panel) {
		activePanel = panel;
		gridElement = panelAttributeProvider;
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
