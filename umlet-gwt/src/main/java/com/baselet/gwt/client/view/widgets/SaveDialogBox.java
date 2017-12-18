package com.baselet.gwt.client.view.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class SaveDialogBox extends MyPopupPanel {

	public static interface Callback {
		void callback(String chosenName);
	}

	private final TextBox textBox = new TextBox();
	private final Button saveButton = new Button("Save");
	private final Button cancelButton = new Button("Cancel");

	public SaveDialogBox(final Callback callback, String headerText) {
		super(true, Type.POPUP);
		initSaveDialogBox(callback, headerText);
	}

	public SaveDialogBox(final Callback callback) {
		super(true, Type.POPUP);
		initSaveDialogBox(callback, "Save Diagram");
	}

	private void initSaveDialogBox(final Callback callback, String headerText) {
		setHeader(headerText);
		textBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					submitDialog(callback);
				}
			}
		});
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				submitDialog(callback);
			}
		});
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		FlowPanel panel = new FlowPanel();
		panel.add(new HTML("Please choose a name"));
		panel.add(textBox);
		FlowPanel fp = new FlowPanel();
		fp.add(saveButton);
		fp.add(cancelButton);
		panel.add(fp);
		setWidget(panel);
	}

	private void submitDialog(final Callback callback) {
		if (textBox.getText().isEmpty()) {
			Window.alert("You must enter a name to save the diagram");
		}
		else {
			hide();
			callback.callback(textBox.getText());
		}
	}

	public void clearAndCenter() {
		center();
		textBox.setFocus(true);
		textBox.selectAll();
	}

	/**
	 * pressing ESC closes the dialogbox
	 */
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		if (event.getTypeInt() == Event.ONKEYDOWN &&
			event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
			hide();
		}
	}
}