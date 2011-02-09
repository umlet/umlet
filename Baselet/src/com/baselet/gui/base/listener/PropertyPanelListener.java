package com.baselet.gui.base.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.ChangeState;
import com.baselet.diagram.command.HelpPanelChanged;
import com.baselet.element.GridElement;

public class PropertyPanelListener implements KeyListener, FocusListener {

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // Esc leaves the property panel
			Main.getInstance().getGUI().requestFocus();
		} else if (!e.isActionKey() && !isUndoRedo(e)) {
			updateGridElement();
		}
	}
	
	private boolean isUndoRedo(KeyEvent e) {
		return (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_Y));
	}

	@Override
	public void focusGained(FocusEvent e) {
		updateGridElement(); // Workaround which is needed to make selection of a autocompletion element via mouse work
	}

	@Override public void focusLost(FocusEvent e) {}
	@Override public void keyTyped(KeyEvent ke) {}
	@Override public void keyPressed(KeyEvent e) {}
	
	private void updateGridElement() {
		GridElement gridElement = Main.getInstance().getEditedGridElement();
		String s = Main.getInstance().getPropertyString();
		DiagramHandler handler = Main.getInstance().getDiagramHandler();
		if (gridElement != null) {
			//only create command if changes were made
			if (!s.equals(gridElement.getPanelAttributes())) {
				int newCaretPos = Main.getInstance().getGUI().getPropertyPane().getCaretPosition();
				int oldCaretPos = newCaretPos - (s.length()-gridElement.getPanelAttributes().length());				
				
				gridElement.getHandler().getController().executeCommand(new ChangeState(gridElement, gridElement.getPanelAttributes(), s, oldCaretPos, newCaretPos));
			}
		}
		else if (handler != null) { // help panel has been edited
			handler.getController().executeCommand(new HelpPanelChanged(s));
		}

		// Scrollbars must be updated cause some entities can grow out of screen border by typing text inside (eg: autoresize custom elements)
		if (handler != null) handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
