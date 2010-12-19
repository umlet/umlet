package com.umlet.gui.standalone;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTabbedPane;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.gui.base.UmletGUI;

public class TabListener implements MouseListener {

	private DiagramHandler handler;
	private JTabbedPane pane;

	public TabListener(DiagramHandler handler, JTabbedPane pane) {
		this.handler = handler;
		this.pane = pane;
	}

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		this.pane.setSelectedComponent(handler.getDrawPanel().getScrollPanel());
		this.handler.getDrawPanel().getSelector().updateSelectorInformation();

		// If this is the standalone client we must also set the zoom value on the upper panel
		UmletGUI gui = Umlet.getInstance().getGUI();
		if ((gui != null) && (gui instanceof StandaloneGUI)) {
			((StandaloneGUI) Umlet.getInstance().getGUI()).setValueOfZoomDisplay(handler.getGridSize());
		}
	}

	public void mouseReleased(MouseEvent arg0) {

	}

}
