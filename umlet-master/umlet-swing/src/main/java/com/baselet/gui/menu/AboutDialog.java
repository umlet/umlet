package com.baselet.gui.menu;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.enums.Program;
import com.baselet.gui.listener.HyperLinkActiveListener;

public class AboutDialog {

	private static final Logger log = LoggerFactory.getLogger(AboutDialog.class);

	public static void show() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final JEditorPane edit = new JEditorPane();
					edit.setBorder(new LineBorder(Color.GRAY, 1, true));
					edit.setPage(AboutDialog.class.getClassLoader().getResource("about.html"));
					edit.addHyperlinkListener(new HyperLinkActiveListener());
					edit.setEditable(false);
					edit.setSelectionColor(Color.WHITE);
					JDialog instance = new JOptionPane(edit, JOptionPane.PLAIN_MESSAGE).createDialog("About " + Program.getInstance().getProgramName());
					instance.setVisible(true);
				} catch (IOException e) {
					log.error(null, e);
				}
			}
		});
	}

}
