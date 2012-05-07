package com.baselet.gui.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.baselet.diagram.DrawPanel;


public class ScrollbarListener implements MouseListener, MouseWheelListener {

	private DrawPanel drawpanel;

	public ScrollbarListener(DrawPanel drawpanel) {
		super();
		this.drawpanel = drawpanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawpanel.updatePanelAndScrollbars();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		drawpanel.updatePanelAndScrollbars();
	}

}
