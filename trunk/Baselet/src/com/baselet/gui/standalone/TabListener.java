package com.baselet.gui.standalone;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTabbedPane;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.base.BaseGUI;


public class TabListener implements MouseListener {

	private DiagramHandler handler;
	private JTabbedPane pane;

	public TabListener(DiagramHandler handler, JTabbedPane pane) {
		this.handler = handler;
		this.pane = pane;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.pane.setSelectedComponent(handler.getDrawPanel().getScrollPane());
		this.handler.getDrawPanel().getSelector().updateSelectorInformation();

		// If this is the standalone client we must also set the zoom value on the upper panel
		BaseGUI gui = Main.getInstance().getGUI();
		if ((gui != null) && (gui instanceof StandaloneGUI)) {
			((StandaloneGUI) Main.getInstance().getGUI()).setValueOfZoomDisplay(handler.getGridSize());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

}
