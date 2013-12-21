package com.baselet.gui.standalone;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.baselet.control.Constants.SystemInfo;
import com.baselet.control.Main;

public class SearchKeyListener implements KeyListener {
	
	@Override public void keyPressed(KeyEvent e) {
		boolean meta_f_pressed = (e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & SystemInfo.META_KEY.getMask()) != 0);
		if (meta_f_pressed || e.getKeyChar() == '/') {
			Main.getInstance().getGUI().enableSearch(true);
		}
	}

	@Override public void keyTyped(KeyEvent e) {}
	@Override public void keyReleased(KeyEvent e) {}
}
