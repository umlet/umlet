package com.baselet.gui.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.baselet.control.Main;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.ChangeState;
import com.baselet.diagram.command.CustomCodePropertyChanged;
import com.baselet.diagram.command.HelpPanelChanged;
import com.baselet.element.GridElement;

public class PropertyPanelListener implements KeyListener, DocumentListener {

	public PropertyPanelListener() {
	}

	@Override public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\u001b') { // ESC Key: Leaves the Property Panel
			Main.getInstance().getGUI().requestFocus();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				updateGridElement();
			}
		});
	}

	protected void updateGridElement() {
		GridElement gridElement = Main.getInstance().getEditedGridElement();
		String s = Main.getInstance().getGUI().getPropertyPane().getText();
		DiagramHandler handler = Main.getInstance().getDiagramHandler();

		if (gridElement != null) {
			//only create command if changes were made
			if (!s.equals(gridElement.getPanelAttributes())) {
				int newCaretPos = Main.getInstance().getGUI().getPropertyPane().getTextComponent().getCaretPosition();
				int oldCaretPos = newCaretPos - (s.length()-gridElement.getPanelAttributes().length());		

				if (Main.getHandlerForElement(gridElement) instanceof CustomPreviewHandler) {
					Main.getHandlerForElement(gridElement).getController().executeCommand(new CustomCodePropertyChanged(gridElement.getPanelAttributes(), s, oldCaretPos, newCaretPos));
				} else {
					Main.getHandlerForElement(gridElement).getController().executeCommand(new ChangeState(gridElement, gridElement.getPanelAttributes(), s, oldCaretPos, newCaretPos));
				}
			}
		}
		else if (handler != null && !s.equals(handler.getHelpText())) { // help panel has been edited
			handler.getController().executeCommand(new HelpPanelChanged(s));
		}

		// Scrollbars must be updated cause some entities can grow out of screen border by typing text inside (eg: autoresize custom elements)
		if (handler != null) handler.getDrawPanel().updatePanelAndScrollbars();
	}

	@Override public void keyReleased(KeyEvent e) {}
	@Override public void keyPressed(KeyEvent e) {}
	@Override public void insertUpdate(DocumentEvent e) {}
	@Override public void removeUpdate(DocumentEvent e) {}
}
