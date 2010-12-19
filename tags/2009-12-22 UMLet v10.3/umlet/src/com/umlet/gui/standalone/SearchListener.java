package com.umlet.gui.standalone;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextField;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.command.Search;
import com.umlet.control.diagram.DiagramHandler;

public class SearchListener implements KeyListener, MouseMotionListener {

	public SearchListener() {}

	public void keyPressed(KeyEvent arg0) {

	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			JTextField search = (JTextField) e.getComponent();
			DiagramHandler h = Umlet.getInstance().getDiagramHandler();
			if ((h != null) && !"".equals(search) && (search != null)) h.getController().executeCommand(new Search(search.getText()));
			Umlet.getInstance().getGUI().enableSearch(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) Umlet.getInstance().getGUI().enableSearch(false);
	}

	public void keyTyped(KeyEvent arg0) {}

	public void mouseDragged(MouseEvent me) {}

	public void mouseMoved(MouseEvent me) {
		Umlet.getInstance().getGUI().setCursor(Constants.TEXT_CURSOR);
	}

}
