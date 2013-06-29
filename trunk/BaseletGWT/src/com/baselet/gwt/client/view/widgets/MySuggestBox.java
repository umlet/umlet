package com.baselet.gwt.client.view.widgets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

public class MySuggestBox extends SuggestBox {
	
	//TODO perhaps use '\t' because its completely invisible
	public final static String COMMENT_CHAR = " ~";

	public MySuggestBox(SuggestOracle oracle, OwnTextArea textArea) {
		super(oracle, textArea);
		
		// if the focus is on the suggestbox avoid propagating certain keyevents to the textarea
		this.addKeyPressHandler(new KeyPressHandler() {
	        @Override
	        public void onKeyPress(KeyPressEvent event) {
	            int key = event.getNativeEvent().getKeyCode();
	            boolean isSuggestBoxControlKey = key == KeyCodes.KEY_ENTER || key == KeyCodes.KEY_UP || key == KeyCodes.KEY_DOWN;
				if (MySuggestBox.this.isSuggestionListShowing() && isSuggestBoxControlKey) {
	            	event.getNativeEvent().preventDefault();
	            }
	        }
	});
	}

	@Override
	public void setText(String text) {
		// only replace current line. Also only use suggestion until COMMENT_CHAR which only contains additional explaination about the gridelement-setting
		super.setText(replaceTextOfCurrentLine(text.substring(0, text.indexOf(COMMENT_CHAR))));
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
			for (String line : wholeText.split("(\r)?\n")) {
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
