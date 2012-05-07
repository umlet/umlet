package com.baselet.gui.standalone;

import java.awt.event.WindowEvent;

import com.baselet.control.Main;


public class WindowListener implements java.awt.event.WindowListener {

	public WindowListener() {}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		Main.getInstance().getGUI().closeWindow();
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
