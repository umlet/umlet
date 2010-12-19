package com.umlet.gui.base.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.umlet.control.diagram.DrawPanel;

public class ScrollbarListener implements MouseListener, MouseWheelListener {

	private DrawPanel drawpanel;

	public ScrollbarListener(DrawPanel drawpanel) {
		super();
		this.drawpanel = drawpanel;
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {
		drawpanel.removeUnnecessaryWhitespaceAroundDiagram();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		drawpanel.removeUnnecessaryWhitespaceAroundDiagram();
	}

}
