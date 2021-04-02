package com.baselet.gwt.client.view.widgets.propertiespanel;

import java.util.ArrayList;
import java.util.List;

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

public abstract class TextArea extends MySuggestBox {
	private static final String DEFAULT_HELPTEXT = "Space for diagram notes";

	protected final OwnTextArea textArea;

	protected final MySuggestOracle oracle;

	protected HasPanelAttributes gridElement;

	protected Redrawable activePanel = null;

	protected String oldText;

	@UiConstructor
	public TextArea() {
		this(new MySuggestOracle(), new OwnTextArea());

		textArea.setInstantValueChangeHandler(new InstantValueChangeHandler() {
			@Override
			public void onValueChange(String value) {
				if (gridElement != null) {
					setGridElementAttributes();
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

	public TextArea(final MySuggestOracle oracle, OwnTextArea textArea) {
		super(oracle, textArea);
		this.oracle = oracle;
		this.textArea = textArea;
	}

	public void setGridElement(HasPanelAttributes panelAttributeProvider, Redrawable panel) {
		activePanel = panel;
		gridElement = panelAttributeProvider;
		String panelAttributes = getGridElementAttributes(panelAttributeProvider);
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

	public abstract String getGridElementAttributes(HasPanelAttributes panelAttributeProvider);

	public abstract void setGridElementAttributes();
}
