package com.baselet.gui.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.Main;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.command.ChangePanelAttributes;
import com.baselet.gui.command.CustomCodePropertyChanged;
import com.baselet.gui.command.HelpPanelChanged;

public class PropertyPanelListener implements KeyListener, DocumentListener {

	public PropertyPanelListener() {}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\u001b') { // ESC Key: Leaves the Property Panel
			CurrentGui.getInstance().getGui().requestFocus();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateGridElement();
			}
		});
	}

	protected void updateGridElement() {
		GridElement gridElement = Main.getInstance().getEditedGridElement();
		String newPannelAttributes = CurrentGui.getInstance().getGui().getPropertyPane().getText();
		String newCustomDrawingAttribtues = CurrentGui.getInstance().getGui().getCustomDrawingsPane().getText();
		DiagramHandler handler = CurrentDiagram.getInstance().getDiagramHandler();

		if (gridElement != null) {
			// only create command if changes were made
			if (!newPannelAttributes.equals(gridElement.getPanelAttributes()) || !newCustomDrawingAttribtues.equals(gridElement.getCustomDrawingsCode())) {
				int newCaretPos = CurrentGui.getInstance().getGui().getPropertyPane().getTextComponent().getCaretPosition();
				int oldCaretPos = newCaretPos - (newPannelAttributes.length() - gridElement.getPanelAttributes().length());

				if (HandlerElementMap.getHandlerForElement(gridElement) instanceof CustomPreviewHandler) {
					HandlerElementMap.getHandlerForElement(gridElement).getController().executeCommand(new CustomCodePropertyChanged(gridElement.getPanelAttributes(), newPannelAttributes, oldCaretPos, newCaretPos));
				}
				else {
					HandlerElementMap.getHandlerForElement(gridElement).getController().executeCommand(
							new ChangePanelAttributes(gridElement, gridElement.getPanelAttributes(), newPannelAttributes, gridElement.getCustomDrawingsCode(), newCustomDrawingAttribtues, oldCaretPos, newCaretPos));
				}
			}
		}
		else if (handler != null && !newPannelAttributes.equals(handler.getHelpText())) { // help panel has been edited
			handler.getController().executeCommand(new HelpPanelChanged(newPannelAttributes));
		}

		// Scrollbars must be updated cause some entities can grow out of screen border by typing text inside (eg: autoresize custom elements)
		if (handler != null) {
			handler.getDrawPanel().updatePanelAndScrollbars();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	}
}
