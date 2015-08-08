package com.baselet.standalone.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTabbedPane;

import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.CurrentGui;

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
		pane.setSelectedComponent(handler.getDrawPanel().getScrollPane());
		handler.getDrawPanel().getSelector().updateSelectorInformation();

		BaseGUI gui = CurrentGui.getInstance().getGui();
		if (gui != null) {
			gui.setValueOfZoomDisplay(handler.getGridSize());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

}
