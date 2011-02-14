package com.baselet.gui;

import java.awt.Color;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.control.Path;
import com.baselet.control.Utils;
import com.baselet.gui.listener.HyperLinkActiveListener;

@SuppressWarnings("serial")
public class AboutPanel extends JPanel {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private static AboutPanel window;

	private AboutPanel() {
		super();
		JEditorPane pane = new JEditorPane();
		pane.setBorder(new LineBorder(Color.GRAY, 1, true));
		showHTML(pane);
		add(pane);
	}

	public static AboutPanel getInstance() {
		if (window == null) window = new AboutPanel();
		return window;
	}

	public void setVisible() {
		Main.getInstance().getGUI().openDialog("About " + Program.PROGRAM_NAME, this);
	}

	protected String getStartUpFileName() {
		return "file:///" + Path.homeProgram() + "html/about.html";
	}

	private void showHTML(JEditorPane edit) {
		try {
			edit.setPage(new URL(getStartUpFileName()));
			edit.addHyperlinkListener(new HyperLinkActiveListener());
			edit.setEditable(false);
			edit.setSelectionColor(Color.WHITE);
		} catch (Exception e) {
			log.error(null, e);
		}
	}

}
