package com.baselet.standalone.gui;

import java.awt.event.WindowEvent;

import com.baselet.gui.CurrentGui;

public class SwingWindowListener implements java.awt.event.WindowListener {

	public SwingWindowListener() {}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		CurrentGui.getInstance().getGui().closeWindow();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}

}
