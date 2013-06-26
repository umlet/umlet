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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SaveDialogBox extends DialogBox {

	public static interface Callback {
		void callback(String chosenName);
	}

	private TextBox textBox = new TextBox();
	private Button saveButton = new Button("Save");
	private Button cancelButton = new Button("Cancel");

	public SaveDialogBox(final Callback callback) {
		super(true);
		setText("Please choose a name");
		setGlassEnabled(true);
		setAnimationEnabled(true);
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
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(textBox);
		FlowPanel fp = new FlowPanel();
		fp.add(saveButton);
		fp.add(cancelButton);
		vPanel.add(fp);
		setWidget(vPanel);
	}

	private void submitDialog(final Callback callback) {
		if (textBox.getText().isEmpty()) {
			Window.alert("You must enter a name to save the diagram");
		} else {
			hide();
			callback.callback(textBox.getText());
		}
	}

	public void clearAndCenter() {
		textBox.setText("");
		center();
		textBox.setFocus(true);
	}

	/**
	 * pressing ESC closes the dialogbox
	 */
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		if (
				event.getTypeInt() == Event.ONKEYDOWN && 
				event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
			hide();
		}
	}
}