package com.baselet.gui.eclipse;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ShowDialog implements Runnable {

	JComponent component;
	String title;

	public ShowDialog(String title, JComponent comp) {
		this.component = comp;
		this.title = title;
	}

	@Override
	public void run() {
		JDialog dialog = (new JOptionPane(component, JOptionPane.PLAIN_MESSAGE))
				.createDialog(title);
		dialog.setVisible(true);
	}

}
