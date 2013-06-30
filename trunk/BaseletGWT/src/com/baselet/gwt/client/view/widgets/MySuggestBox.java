package com.baselet.gwt.client.view.widgets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

public class MySuggestBox extends SuggestBox {

	public MySuggestBox(SuggestOracle oracle, OwnTextArea textArea) {
		super(oracle, textArea);
		
		this.addKeyPressHandler(new KeyPressHandler() {
	        @Override
	        public void onKeyPress(KeyPressEvent event) {
	            int key = event.getNativeEvent().getKeyCode();
	            if (key == KeyCodes.KEY_ESCAPE) {
	            	MySuggestBox.this.hideSuggestionList();
	            }
	            else if (MySuggestBox.this.isSuggestionListShowing() && (key == KeyCodes.KEY_ENTER || key == KeyCodes.KEY_UP || key == KeyCodes.KEY_DOWN)) {
	            	event.getNativeEvent().preventDefault(); // if the focus is on the suggestbox avoid propagating certain keyevents to the textarea
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
		return getCursorLine();
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
			returnText = wholeText;
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
