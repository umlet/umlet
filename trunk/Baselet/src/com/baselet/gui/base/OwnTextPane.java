package com.baselet.gui.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import com.baselet.control.Constants;


@SuppressWarnings("serial")
public class OwnTextPane extends JTextPane {

	private ArrayList<String> specialcommands;
	private JPanel panel;

	public OwnTextPane(JPanel panel) {
		specialcommands = new ArrayList<String>();
		specialcommands.add("bg=");
		specialcommands.add("fg=");
		specialcommands.add(Constants.AUTORESIZE);
		this.panel = panel;
		this.setBackground(Color.WHITE);
		this.setComponentPopupMenu(new TextPopupMenu(this));

		StyledDocument doc = this.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		doc.addStyle("default", def);
		Style err = doc.addStyle("special", def);
		StyleConstants.setForeground(err, Color.gray);

		setTabSize(3, 40);
	}

	// AB: There should be a better solution; this inserts only x tabStops
	// It should somehow be possible to set a general tab size
	private void setTabSize(int tabSize, int tabStops) {
		FontMetrics fm = this.getFontMetrics(this.getFont());

		// use the width of 'm' to align tabSize
		int charWidth = fm.charWidth('m');
		int tabLength = charWidth * tabSize;

		// insert the number of tabStops
		TabStop[] tabs = new TabStop[tabStops];
		for (int j = 0; j < tabs.length; j++) {
			tabs[j] = new TabStop((j + 1) * tabLength);
		}

		TabSet tabSet = new TabSet(tabs);
		Style style = this.getLogicalStyle();
		StyleConstants.setTabSet(style, tabSet);
		this.setLogicalStyle(style);
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
