package com.baselet.gwt.client.view.widgets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class MySuggestBox extends SuggestBox {

	private String lastRequestLine;

	public MySuggestBox(final MySuggestOracle oracle, ValueBoxBase<String> textArea, final DefaultSuggestionDisplay display) {
		super(oracle, textArea, display);
		
		getValueBox().addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				showSuggestionList(); // switching line of textarea by click should also trigger the suggestbox
			}
		});

		this.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				int key = event.getNativeEvent().getKeyCode();
				boolean isSuggestBoxControls = key == KeyCodes.KEY_ENTER || key == KeyCodes.KEY_UP || key == KeyCodes.KEY_DOWN;
				if (key == KeyCodes.KEY_ESCAPE) {
					display.hideSuggestions();
				}
				else if (display.isSuggestionListShowing() && isSuggestBoxControls && oracle.getSuggestionsForText(lastRequestLine).size() > 1) {
					event.getNativeEvent().preventDefault(); // if the focus is on the suggestbox and it has >1 entries, avoid propagating keyevents to the textarea
				}
			}
		});
	}

	@Override
	public void setText(String text) {
		super.setText(replaceTextOfCurrentLine(text));
	}

	@Override
	public String getText() {
		lastRequestLine = getCursorLine();
		return lastRequestLine;
	}

	private String getCursorLine() {
		String returnText = "";
		String wholeText = super.getText();
		if (!wholeText.contains("\n")) {
			returnText = wholeText;
		} else {
			int cursorPos = getValueBox().getCursorPos();
			int currentPos = 0;
			for (String line : wholeText.split("(\r)?\n")) {
				currentPos += line.length()+1;
				if (cursorPos < currentPos) {
					returnText = line;
					break;
				}
			}
		}
		return returnText;
	}

	private String replaceTextOfCurrentLine(String newText) {
		String returnText = "";
		String wholeText = super.getText();
		if (!wholeText.contains("\n")) {
			returnText = newText;
		} else {
			boolean replaced = false;
			int cursorPos = getValueBox().getCursorPos();
			int currentPos = 0;
			for (String line : wholeText.split("(\r)?\n", -1)) {
				currentPos += line.length()+1;
				if (cursorPos < currentPos && !replaced) {
					returnText += newText + "\n";
					replaced = true;
				} else {
					returnText += line + "\n";
				}
			}
			returnText = returnText.substring(0, returnText.length()-1);
		}
		return returnText;
	}
}
