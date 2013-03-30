package com.baselet.gui;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

import com.baselet.control.Constants.Program;
import com.baselet.control.Path;
import com.baselet.control.Utils;
import com.baselet.gui.listener.HyperLinkActiveListener;

public class AboutDialog {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	public static void show() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final JEditorPane edit = new JEditorPane();
					edit.setBorder(new LineBorder(Color.GRAY, 1, true));
					edit.setPage(new URL("file:///" + Path.homeProgram() + "html/about.html"));
					edit.addHyperlinkListener(new HyperLinkActiveListener());
					edit.setEditable(false);
					edit.setSelectionColor(Color.WHITE);
					JDialog instance = (new JOptionPane(edit, JOptionPane.PLAIN_MESSAGE)).createDialog("About " + Program.PROGRAM_NAME);
					instance.setVisible(true);
				} catch (IOException e) {
					log.error(null, e);
				}
			}
		});
	}

}
