package com.umlet.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.umlet.control.Umlet;

public class TextpaneFocusListener implements FocusListener {

	public void focusGained(FocusEvent e) {
		Umlet.getInstance().getGUI().setTextPanelFocused(true);
	}

	public void focusLost(FocusEvent e) {
		Umlet.getInstance().getGUI().setTextPanelFocused(false);
	}

}
