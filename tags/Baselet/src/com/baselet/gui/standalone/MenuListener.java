package com.baselet.gui.standalone;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

import com.baselet.control.Constants;
import com.baselet.control.Main;


public class MenuListener implements MouseListener, MouseMotionListener {

	private static MenuListener _menulistener;

	public static MenuListener getInstance() {
		if (_menulistener == null) _menulistener = new MenuListener();
		return _menulistener;
	}

	protected MenuListener() {}

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
		if (!label.getText().startsWith("Discard")) Main.getInstance().getGUI().getCurrentCustomHandler().saveEntity();
		if (Main.getInstance().getGUI().getCurrentCustomHandler().closeEntity()) {
			Main.getInstance().getGUI().setCustomPanelEnabled(false);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}

	@Override
	public void mouseDragged(MouseEvent me) {

	}

	@Override
	public void mouseMoved(MouseEvent me) {

	}
}
