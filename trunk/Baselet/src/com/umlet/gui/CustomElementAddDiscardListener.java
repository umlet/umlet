package com.umlet.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import com.baselet.control.Constants;
import com.baselet.control.Main;

public class CustomElementAddDiscardListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent me) {

	}

	@Override
	public void mouseEntered(MouseEvent me) {
		Main.getInstance().getGUI().setCursor(Constants.HAND_CURSOR);
		JLabel label = (JLabel) me.getComponent();
		label.setForeground(Color.blue);
	}

	@Override
	public void mouseExited(MouseEvent me) {
		Main.getInstance().getGUI().setCursor(Constants.DEFAULT_CURSOR);
		JLabel label = (JLabel) me.getComponent();
		label.setForeground(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		JLabel label = (JLabel) me.getComponent();
		if (!label.getText().startsWith("Discard")) {
			Main.getInstance().getGUI().getCurrentCustomHandler().saveEntity();
		}
		if (Main.getInstance().getGUI().getCurrentCustomHandler().closeEntity()) {
			Main.getInstance().getGUI().setCustomPanelEnabled(false);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}
}
