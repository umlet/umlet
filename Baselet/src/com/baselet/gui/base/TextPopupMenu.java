package com.baselet.gui.base;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

@SuppressWarnings("serial")
public class TextPopupMenu extends JPopupMenu {

	private JTextPane textPane;
	private JMenuItem cutText;
	private JMenuItem copyText;
	private JMenuItem pasteText;
	private JMenuItem deleteText;

	public TextPopupMenu(JTextPane textPane) {
		this.textPane = textPane;

		initPopupMenu();
	}

	private void initPopupMenu() {
		cutText = new JMenuItem();
		copyText = new JMenuItem();
		pasteText = new JMenuItem();
		deleteText = new JMenuItem();

		cutText.setText("Cut");
		copyText.setText("Copy");
		pasteText.setText("Paste");
		deleteText.setText("Delete");

		cutText.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
		copyText.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
		pasteText.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
		deleteText.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));

		cutText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
				{
				textPane.cut();
			}
		});

		copyText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
				{
				textPane.copy();
			}
		});

		pasteText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
				{
				textPane.paste();
			}
		});

		deleteText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
				{
				textPane.replaceSelection("");
			}
		});

		add(cutText);
		add(copyText);
		add(pasteText);
		add(deleteText);

		addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// En-/Disable Menu Items
				updatePopupMenu();
			}
		});
	}

	private void updatePopupMenu() {
		// enable cut/copy/delete if text is selected
		boolean enable = (textPane.getSelectedText() != null);
		cutText.setEnabled(enable);
		copyText.setEnabled(enable);
		deleteText.setEnabled(enable);

		// enable paste if text is in clipboard
		enable = (Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null) != null);
		pasteText.setEnabled(enable);
	}
}
