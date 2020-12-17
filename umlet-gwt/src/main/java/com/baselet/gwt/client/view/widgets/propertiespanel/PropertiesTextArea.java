package com.baselet.gwt.client.view.widgets.propertiespanel;

import com.baselet.element.interfaces.HasPanelAttributes;
import com.baselet.gui.AutocompletionText;
import com.baselet.gwt.client.view.DrawPanelDiagram;
import com.baselet.gwt.client.view.interfaces.Redrawable;
import com.baselet.gwt.client.view.widgets.OwnTextArea;
import com.baselet.gwt.client.view.widgets.OwnTextArea.InstantValueChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiConstructor;

import java.util.ArrayList;
import java.util.List;

public class PropertiesTextArea extends MySuggestBox {

	private static final String DEFAULT_HELPTEXT = "Space for diagram notes";

	private final OwnTextArea textArea;

	private final MySuggestOracle oracle;

	private HasPanelAttributes gridElement;

	private Redrawable activePanel = null;

	private String oldText;

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

				if (oldText != null && !oldText.equals(value)) {
					oldText = value;

					// Update the file-in progress for vscode on any value changes
					if (activePanel instanceof DrawPanelDiagram) {
						((DrawPanelDiagram) activePanel).handleFileUpdate();
					}
				}
			}
		});

		textArea.addKeyDownHandler(new KeyDownHandler() { // CTRL+Space shows all suggestions
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int key = event.getNativeEvent().getKeyCode();
				if (key == KeyCodes.KEY_TAB) {
					event.getNativeEvent().preventDefault();
					int cursorPos = textArea.getCursorPos();
					int cursorPosLine = getCursorPositionInLine();
					String currentTextLine = getCursorLine();
					setText(currentTextLine.substring(0, cursorPosLine) + "\t" + currentTextLine.substring(cursorPosLine));
					textArea.setCursorPos(cursorPos + 1);
				}
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
		oldText = panelAttributes;
		List<AutocompletionTextGwt> autocompletionTextList = new ArrayList<>();
		for (AutocompletionText oldText : panelAttributeProvider.getAutocompletionList()) {
			AutocompletionTextGwt newText = new AutocompletionTextGwt(oldText.getText(), oldText.getInfo(), oldText.getBase64Img());
			autocompletionTextList.add(newText);
		}
		oracle.setAutocompletionList(autocompletionTextList);
	}

	private int getCursorPositionInLine() {
		String wholeText = textArea.getText();
		int cursorPos = textArea.getCursorPos();
		int returnPos = 0;
		if (!wholeText.contains("\n")) {
			return cursorPos;
		}
		else {
			int currentPos = 0;
			for (String line : wholeText.split("(\r)?\n")) {
				int oldPos = currentPos;
				currentPos += line.length() + 1;
				if (cursorPos < currentPos) {
					returnPos = cursorPos - oldPos;
					break;
				}
			}
		}
		return returnPos;
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
