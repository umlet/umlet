package com.umlet.gui.base;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

@SuppressWarnings("serial")
public class CustomCodeTextPane extends UmletTextPane {

	private JToolTip tooltip;

	public CustomCodeTextPane(JPanel panel) {
		super(panel);
		// Create one of each type of tab stop
		ArrayList<TabStop> list = new ArrayList<TabStop>();

		// Create a left-aligned tab stop at 100 pixels from the left margin
		float pos = 10;
		int align = TabStop.ALIGN_LEFT;
		int leader = TabStop.LEAD_NONE;
		TabStop tstop = new TabStop(pos, align, leader);
		list.add(tstop);

		// Create a tab set from the tab stops
		TabStop[] tstops = list.toArray(new TabStop[0]);
		TabSet tabs = new TabSet(tstops);

		// Add the tab set to the logical style;
		// the logical style is inherited by all paragraphs
		Style style = this.getLogicalStyle();
		StyleConstants.setTabSet(style, tabs);
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
