package com.umlet.gui.base;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.umlet.constants.Constants;
import com.umlet.gui.eclipse.TextPaneFocusListener;

@SuppressWarnings("serial")
public class UmletTextPane extends JTextPane {

	private ArrayList<String> specialcommands;
	private JPanel panel;

	public UmletTextPane(JPanel panel) {
		specialcommands = new ArrayList<String>();
		specialcommands.add("bg=");
		specialcommands.add("fg=");
		specialcommands.add(Constants.AUTORESIZE);
		this.panel = panel;
		this.setBackground(Color.WHITE);
		this.addFocusListener(new TextPaneFocusListener());

		StyledDocument doc = this.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		doc.addStyle("default", def);
		Style err = doc.addStyle("special", def);
		StyleConstants.setForeground(err, Color.gray);
	}

	@Override
	public void setSize(Dimension d) {
		if (d.width < getParent().getSize().width) d.width = getParent().getSize().width;

		super.setSize(d);
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public void checkPanelForSpecialChars() {
		String[] lines = this.getText().split(Constants.NEWLINE, -1);
		for (int linenum = 0, pos = 0; linenum < lines.length; linenum++) {
			String line = lines[linenum];
			boolean special = false;
			for (String comp : this.specialcommands) {
				if (line.startsWith(comp)) {
					special = true;
					break;
				}
			}
			if (special) this.getStyledDocument().setCharacterAttributes(pos, line.length(), this.getStyledDocument().getStyle("special"), true);
			else this.getStyledDocument().setCharacterAttributes(pos, line.length(), this.getStyledDocument().getStyle("default"), true);

			pos += line.length() + 1;
		}
	}

	public JPanel getPanel() {
		return this.panel;
	}
}
