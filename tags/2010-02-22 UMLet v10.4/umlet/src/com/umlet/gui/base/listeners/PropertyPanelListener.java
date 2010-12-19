package com.umlet.gui.base.listeners;

import java.awt.event.ComponentAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.umlet.control.Umlet;
import com.umlet.control.command.ChangeState;
import com.umlet.control.command.HelpPanelChanged;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;
import com.umlet.gui.base.UmletTextPane;

public class PropertyPanelListener extends ComponentAdapter implements KeyListener {

	private UmletTextPane pane;

	public PropertyPanelListener(UmletTextPane pane) {
		this.pane = pane;
	}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {

		// Esc leaves the property panel
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Umlet.getInstance().getGUI().requestFocus();
		}
		else {
			Entity entity = Umlet.getInstance().getEditedEntity(); // LME
			String s = Umlet.getInstance().getPropertyString();
			DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
			if (entity != null) {
				entity.getHandler().getController().executeCommand(new ChangeState(entity, entity.getPanelAttributes(), s));
			}
			else if (handler != null) // help panel has been edited
			handler.getController().executeCommand(new HelpPanelChanged(s));

			this.pane.checkPanelForSpecialChars();
			e.consume();

			// Scrollbars must be updated cause some entities can grow out of screen border by typing text inside (eg: autoresize custom elements)
			handler.getDrawPanel().updatePanelAndScrollbars();
		}
	}

	public void keyTyped(KeyEvent ke) {}

}
