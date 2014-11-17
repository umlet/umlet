package com.baselet.gwt.client.view.widgets.propertiespanel;

import com.baselet.gwt.client.keyboard.Shortcut;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class MySuggestBox extends SuggestBox {

	private String lastRequestLine;
	private final MySuggestionDisplay display;

	public MySuggestBox(final MySuggestOracle oracle, ValueBoxBase<String> textArea) {
		this(oracle, textArea, new MySuggestionDisplay());
	}

	public MySuggestBox(final MySuggestOracle oracle, ValueBoxBase<String> textArea, final MySuggestionDisplay display) {
		super(oracle, textArea, display);
		this.display = display;

		getValueBox().addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				showSuggestionList(); // switching line of textarea by click should also update the shown suggestions
			}
		});

		textArea.addKeyDownHandler(new KeyDownHandler() { // CTRL+Space shows all suggestions
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int key = event.getNativeEvent().getKeyCode();
				if (display.isSuggestionListShowing()) {
					if (key == KeyCodes.KEY_ESCAPE) {
						display.hideSuggestions();
					}
					else if ((key == KeyCodes.KEY_ENTER || key == KeyCodes.KEY_UP || key == KeyCodes.KEY_DOWN) && oracle.getSuggestionsForText(lastRequestLine).size() > 1) {
						event.getNativeEvent().preventDefault(); // if the focus is on the suggestbox and it has >1 entries, avoid propagating keyevents to the textarea
					}
				}
				if (Shortcut.SHOW_AUTOCOMPLETION.matches(event)) {
					event.getNativeEvent().preventDefault();
					oracle.setShowAllAsDefault(true);
					showSuggestionList();
					oracle.setShowAllAsDefault(false);
				}
			}
		});
	}

	@Override
	public void setText(String text) {
		super.setText(replaceTextOfCurrentLine(text));
		setFocus(true); // after choosing one of the suggestions, refocus the textarea
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
		}
		else {
			int cursorPos = getValueBox().getCursorPos();
			int currentPos = 0;
			for (String line : wholeText.split("(\r)?\n")) {
				currentPos += line.length() + 1;
				if (cursorPos < currentPos) {
					returnText = line;
					break;
				}
			}
		}
		return returnText;
	}

	private String replaceTextOfCurrentLine(String newText) {
		StringBuilder sb = new StringBuilder("");
		String wholeText = super.getText();
		if (!wholeText.contains("\n")) {
			sb.append(newText);
		}
		else {
			boolean replaced = false;
			int cursorPos = getValueBox().getCursorPos();
			int currentPos = 0;
			for (String line : wholeText.split("(\r)?\n", -1)) {
				currentPos += line.length() + 1;
				if (cursorPos < currentPos && !replaced) {
					sb.append(newText).append("\n");
					replaced = true;
				}
				else {
					sb.append(line).append("\n");
				}
			}
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	public boolean getPaletteShouldIgnoreMouseClicks() {
		return display.getPaletteShouldIgnoreMouseClicks();
	}
}
