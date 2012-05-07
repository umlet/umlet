package com.baselet.diagram.command;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;


public class HelpPanelChanged extends Command {
	private String changed_to;
	private String changed_from;

	public HelpPanelChanged(String text) {
		this.changed_to = text;
	}

	private HelpPanelChanged(String changed_from, String changed_to) {
		this.changed_from = changed_from;
		this.changed_to = changed_to;
	}

	public static Integer getFontsize(String text) {
		if (text == null) return null;
		Pattern p = Pattern.compile("fontsize=([0-9]+)( .*)?");
		Vector<String> txt = Utils.decomposeStrings(text);
		for (String t : txt) {
			Matcher m = p.matcher(t);
			if (m.matches()) {
				if (t.contains("//")) t = t.split("//")[0];
				return Integer.parseInt(m.group(1));
			}
		}
		return null;
	}

	public static String getFontfamily(String text) {
		if (text == null) return null;
		Pattern p = Pattern.compile("fontfamily\\=(\\w+).*");
		Vector<String> txt = Utils.decomposeStrings(text);
		for (String t : txt) {
			Matcher m = p.matcher(t);
			if (m.matches()) {
				if (t.contains("//")) t = t.split("//")[0];
				return m.group(1);
			}
		}
		return null;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		this.changed_from = handler.getHelpText();
		handler.setHelpText(changed_to);
		Integer fontsize = getFontsize(changed_to);
		if (fontsize != null) handler.getFontHandler().setDiagramDefaultFontSize(fontsize);
		else handler.getFontHandler().resetDiagramDefaultFontSize();
		String fontfamily = getFontfamily(changed_to);
		if (fontfamily != null) handler.getFontHandler().setDiagramDefaultFontFamily(fontfamily);
		else handler.getFontHandler().resetDiagramDefaultFontFamily();
		handler.getDrawPanel().repaint();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		handler.setHelpText(changed_from);
		handler.getFontHandler().setDiagramDefaultFontSize(getFontsize(changed_from));
		handler.getDrawPanel().repaint();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if ((c instanceof HelpPanelChanged)) return true;
		return false;
	}

	@Override
	public Command mergeTo(Command c) {
		HelpPanelChanged tmp = (HelpPanelChanged) c;
		HelpPanelChanged ret = new HelpPanelChanged(tmp.changed_from, this.changed_to);
		return ret;
	}
}
