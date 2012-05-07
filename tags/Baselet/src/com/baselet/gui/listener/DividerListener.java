package com.baselet.gui.listener;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.PaletteHandler;


public class DividerListener implements ComponentListener {

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentResized(ComponentEvent e) {
		PaletteHandler p = Main.getInstance().getPalette();
		if ((p != null) && (p.getDrawPanel() != null)) p.getDrawPanel().updatePanelAndScrollbars();
		DiagramHandler h = Main.getInstance().getDiagramHandler();
		if ((h != null) && (h.getDrawPanel() != null)) h.getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public void componentShown(ComponentEvent e) {}

}
