package com.baselet.custom;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import com.baselet.control.basics.Converter;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.gui.CurrentGui;

public class CustomElementAddDiscardListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent me) {

	}

	@Override
	public void mouseEntered(MouseEvent me) {
		CurrentGui.getInstance().getGui().setCursor(Converter.convert(CursorOwn.HAND));
		JLabel label = (JLabel) me.getComponent();
		label.setForeground(Color.blue);
	}

	@Override
	public void mouseExited(MouseEvent me) {
		CurrentGui.getInstance().getGui().setCursor(Converter.convert(CursorOwn.DEFAULT));
		JLabel label = (JLabel) me.getComponent();
		label.setForeground(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		JLabel label = (JLabel) me.getComponent();
		if (!label.getText().startsWith("Discard")) {
			CurrentGui.getInstance().getGui().getCurrentCustomHandler().saveEntity();
		}
		if (CurrentGui.getInstance().getGui().getCurrentCustomHandler().closeEntity()) {
			CurrentGui.getInstance().getGui().setCustomPanelEnabled(false);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}
}
