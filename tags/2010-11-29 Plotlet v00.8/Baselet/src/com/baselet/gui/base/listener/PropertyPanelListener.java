package com.baselet.gui.base.listener;

import java.awt.event.ComponentAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.ChangeState;
import com.baselet.diagram.command.HelpPanelChanged;
import com.baselet.element.GridElement;
import com.baselet.gui.base.OwnSyntaxPane;


public class PropertyPanelListener implements KeyListener, FocusListener {

	private OwnSyntaxPane pane;

	public PropertyPanelListener(OwnSyntaxPane pane) {
		this.pane = pane;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // Esc leaves the property panel
			Main.getInstance().getGUI().requestFocus();
		}
		else updateGridElement();
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
			gridElement.getHandler().getController().executeCommand(new ChangeState(gridElement, gridElement.getPanelAttributes(), s));
		}
		else if (handler != null) { // help panel has been edited
			handler.getController().executeCommand(new HelpPanelChanged(s));
		}

		this.pane.checkPanelForSpecialChars();

		// Scrollbars must be updated cause some entities can grow out of screen border by typing text inside (eg: autoresize custom elements)
		if (handler != null) handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
