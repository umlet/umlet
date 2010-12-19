package com.baselet.gui.base;

import java.awt.FontMetrics;

import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

@SuppressWarnings("serial")
public class CustomCodeTextPane extends OwnTextPane {

	private JToolTip tooltip;

	public CustomCodeTextPane(JPanel panel) {
		super(panel);

		// AB: New tabstop solution; old one was buggy
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
	public JToolTip createToolTip() {
		tooltip = new JMultiLineToolTip();
		return tooltip;
	}

	public JToolTip getToolTip() {
		return this.tooltip;
	}
}
