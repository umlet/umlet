package com.umlet.gui.base.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;

public class GUIListener implements KeyListener {

	public void keyPressed(KeyEvent e) {

		// Any letter: Jumps directly into the properties pane
		// The direct jump into the property pane is only allowed if no command button except shift is pressed
		if (!e.isAltDown() && !e.isControlDown() && !e.isMetaDown() && !e.isAltGraphDown()) {
			if (String.valueOf(e.getKeyChar()).matches("[a-zA-Z0-9_ äüöß]") ||
					(e.getKeyCode() == KeyEvent.VK_ENTER) ||
					(e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_KP_DOWN) ||
					(e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_KP_UP) ||
					(e.getKeyCode() == KeyEvent.VK_LEFT) || (e.getKeyCode() == KeyEvent.VK_KP_LEFT) ||
					(e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_KP_RIGHT)) {
				Umlet.getInstance().getGUI().focusPropertyPaneAndWriteCharacter(e.getKeyCode());
			}
		}

		// Ctrl +/-: Diagram zoom
		// Only if Ctrl or no command button is pressed while scrolling, we zoom in and out
		if ((e.getModifiersEx() == 0) || ((e.getModifiersEx() & Constants.CTRLMETA_DOWN_MASK) == Constants.CTRLMETA_DOWN_MASK)) {
			if (Umlet.getInstance().getDiagramHandler() != null) {
				// KeyChar check doesn't check non-numpad + on some keyboards, therefore we also need KeyEvent.VK_PLUS
				if ((e.getKeyChar() == '+') || (e.getKeyCode() == KeyEvent.VK_PLUS)) {
					int actualZoom = Umlet.getInstance().getDiagramHandler().getGridSize();
					Umlet.getInstance().getDiagramHandler().setGridAndZoom(actualZoom + 1);
				}
				// KeyChar check doesn't check non-numpad - on some keyboards, therefore we also need KeyEvent.VK_MINUS
				else if ((e.getKeyChar() == '-') || (e.getKeyCode() == KeyEvent.VK_MINUS)) {
					int actualZoom = Umlet.getInstance().getDiagramHandler().getGridSize();
					Umlet.getInstance().getDiagramHandler().setGridAndZoom(actualZoom - 1);
				}
			}
		}

		// TODO implement ESC Overview
		// if ((e.getModifiersEx() == 0) && (e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
		// if ((Umlet.getInstance().getDiagramHandler() != null) && (Umlet.getInstance().getDiagramHandler().getDrawPanel() != null)) {
		// // As long as the horizontal or vertical scrollbar is visible we must zoom 1 step out
		// while (Umlet.getInstance().getDiagramHandler().getDrawPanel().getScrollPanel().getHorizontalScrollBar().isVisible() ||
		// Umlet.getInstance().getDiagramHandler().getDrawPanel().getScrollPanel().getVerticalScrollBar().isVisible()) {
		// int actualZoom = Umlet.getInstance().getDiagramHandler().getGridSize();
		// Umlet.getInstance().getDiagramHandler().setGridAndZoom(actualZoom - 1);
		// }
		// }
		// }

	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}

}
