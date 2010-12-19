package com.umlet.gui.standalone;

import java.awt.event.WindowEvent;

import com.umlet.control.Umlet;

public class WindowListener implements java.awt.event.WindowListener {

	public WindowListener() {}

	public void windowActivated(WindowEvent arg0) {}

	public void windowClosed(WindowEvent arg0) {}

	public void windowClosing(WindowEvent arg0) {
		Umlet.getInstance().getGUI().closeWindow();
	}

	public void windowDeactivated(WindowEvent arg0) {}

	public void windowDeiconified(WindowEvent arg0) {}

	public void windowIconified(WindowEvent arg0) {}

	public void windowOpened(WindowEvent arg0) {}

}
