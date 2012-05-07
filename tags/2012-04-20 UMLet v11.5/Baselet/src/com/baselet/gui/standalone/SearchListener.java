package com.baselet.gui.standalone;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextField;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.Search;


public class SearchListener implements KeyListener, MouseMotionListener {

	public SearchListener() {}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			JTextField search = (JTextField) e.getComponent();
			DiagramHandler h = Main.getInstance().getDiagramHandler();
			if ((h != null) && !"".equals(search) && (search != null)) h.getController().executeCommand(new Search(search.getText()));
			Main.getInstance().getGUI().enableSearch(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) Main.getInstance().getGUI().enableSearch(false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void mouseDragged(MouseEvent me) {}

	@Override
	public void mouseMoved(MouseEvent me) {
		Main.getInstance().getGUI().setCursor(Constants.TEXT_CURSOR);
	}

}
