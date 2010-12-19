package com.baselet.gui.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.text.TabStop;

import jsyntaxpane.DefaultSyntaxKit;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Utils;
import com.plotlet.gui.PlotletSyntaxKit;


@SuppressWarnings("serial")
public class OwnSyntaxPane extends JEditorPane {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private ArrayList<String> specialcommands;
	private JPanel panel;

	public OwnSyntaxPane(JPanel panel) {
		specialcommands = new ArrayList<String>();
		specialcommands.add("bg=");
		specialcommands.add("fg=");
		specialcommands.add(Constants.AUTORESIZE);
		this.panel = panel;
		this.setBackground(Color.WHITE);
		//		this.setComponentPopupMenu(new TextPopupMenu(this));

		//		StyledDocument doc = this.getStyledDocument();
		//		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		//		doc.addStyle("default", def);
		//		Style err = doc.addStyle("special", def);
		//		StyleConstants.setForeground(err, Color.gray);

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

		//		TabSet tabSet = new TabSet(tabs);
		//		Style style = this.getLogicalStyle();
		//		StyleConstants.setTabSet(style, tabSet);
		//		this.setLogicalStyle(style);
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
		//		String[] lines = this.getText().split(Constants.NEWLINE, -1);
		//		for (int linenum = 0, pos = 0; linenum < lines.length; linenum++) {
		//			String line = lines[linenum];
		//			boolean special = false;
		//			for (String comp : this.specialcommands) {
		//				if (line.startsWith(comp)) {
		//					special = true;
		//					break;
		//				}
		//			}
		//			if (special) this.getStyledDocument().setCharacterAttributes(pos, line.length(), this.getStyledDocument().getStyle("special"), true);
		//			else this.getStyledDocument().setCharacterAttributes(pos, line.length(), this.getStyledDocument().getStyle("default"), true);
		//
		//			pos += line.length() + 1;
		//		}
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void initJSyntaxPane() {
		DefaultSyntaxKit.initKit();
		try {
			// IMPORTANT: The config-key "Action.combo-completion.Items" only accepts a semikolon-separated string because we have changed the method:
			//            jsyntaxpane/actions/ComboCompletionAction.java#setItems(). Otherwise it would only accept a real list
			String autocompletionList = PlotletSyntaxKit.createAutocompletionList(";");
			DefaultSyntaxKit.getConfig(PlotletSyntaxKit.class).put("Action.combo-completion.ItemsAsString", autocompletionList);
		} catch (Exception e) {
			log.error("Error at creating the autocompletion");
		}
		DefaultSyntaxKit.registerContentType("text/propertypanel", PlotletSyntaxKit.class.getCanonicalName());
		this.setContentType("text/propertypanel");
		this.validate();
	}
}
