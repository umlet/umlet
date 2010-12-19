package com.umlet.gui.standalone;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.umlet.control.Umlet;

public class DividerListener implements ComponentListener {

	public void componentHidden(ComponentEvent e) {}

	public void componentMoved(ComponentEvent e) {
		Umlet.getInstance().getGUI().revalidateAllPanels();
	}

	public void componentResized(ComponentEvent e) {}

	public void componentShown(ComponentEvent e) {}

}
