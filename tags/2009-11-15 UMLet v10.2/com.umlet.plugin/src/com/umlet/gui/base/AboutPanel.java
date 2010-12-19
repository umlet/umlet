package com.umlet.gui.base;

import java.awt.Color;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.umlet.control.Umlet;
import com.umlet.gui.base.listeners.HyperLinkActiveListener;

@SuppressWarnings("serial")
public class AboutPanel extends JPanel {

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
		Umlet.getInstance().getGUI().openDialog("About UMLet", this);
	}

	protected String getStartUpFileName() {
		return "file:///" + Umlet.getInstance().getHomePath() + "html/aboutumlet.html";
	}

	private void showHTML(JEditorPane edit) {
		try {
			edit.setPage(new URL(getStartUpFileName()));
			edit.addHyperlinkListener(new HyperLinkActiveListener());
			edit.setEditable(false);
			edit.setSelectionColor(Color.WHITE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
