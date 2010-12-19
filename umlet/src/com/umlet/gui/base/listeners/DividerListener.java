package com.umlet.gui.base.listeners;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.PaletteHandler;

public class DividerListener implements ComponentListener {

	public void componentHidden(ComponentEvent e) {}

	public void componentMoved(ComponentEvent e) {}

	public void componentResized(ComponentEvent e) {
		PaletteHandler p = Umlet.getInstance().getPalette();
		if ((p != null) && (p.getDrawPanel() != null)) p.getDrawPanel().updatePanelAndScrollbars();
		DiagramHandler h = Umlet.getInstance().getDiagramHandler();
		if ((h != null) && (h.getDrawPanel() != null)) h.getDrawPanel().updatePanelAndScrollbars();
	}

	public void componentShown(ComponentEvent e) {}

}
