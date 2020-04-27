package com.baselet.standalone.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextField;

import com.baselet.control.basics.Converter;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.command.Search;

public class SearchListener implements KeyListener, MouseMotionListener {

	public SearchListener() {}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			JTextField search = (JTextField) e.getComponent();
			DiagramHandler h = CurrentDiagram.getInstance().getDiagramHandler();
			if (h != null && search != null && !"".equals(search.getText())) {
				h.getController().executeCommand(new Search(search.getText()));
			}
			CurrentGui.getInstance().getGui().enableSearch(false);
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			CurrentGui.getInstance().getGui().enableSearch(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void mouseDragged(MouseEvent me) {}

	@Override
	public void mouseMoved(MouseEvent me) {
		CurrentGui.getInstance().getGui().setCursor(Converter.convert(CursorOwn.TEXT));
	}

}
