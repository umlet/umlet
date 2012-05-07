package com.baselet.gui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import com.baselet.control.BrowserLauncher;


public class JLink extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;

	public JLink(String url, String text) {
		super("<html><body><a href=\"\">" + text + "</a></body></html>");
		this.url = url;

		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setToolTipText(url);
		addMouseListener(new LinkMouseAdapter());
	}

	private class LinkMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() > 0) BrowserLauncher.openURL(url);
		}
	}
}
