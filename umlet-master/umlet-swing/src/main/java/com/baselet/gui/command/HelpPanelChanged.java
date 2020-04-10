package com.baselet.gui.command;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baselet.control.constants.Constants;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;

public class HelpPanelChanged extends Command {
	private final String changed_to;
	private String changed_from;

	public HelpPanelChanged(String text) {
		changed_to = text;
	}

	private HelpPanelChanged(String changed_from, String changed_to) {
		this.changed_from = changed_from;
		this.changed_to = changed_to;
	}

	public static Double getFontsize(String text) {
		if (text == null) {
			return null;
		}
		Pattern p = Pattern.compile("fontsize=" + Constants.REGEX_FLOAT + "( .*)?");
		Vector<String> txt = Utils.decomposeStrings(text);
		for (String t : txt) {
			Matcher m = p.matcher(t);
			if (m.matches()) {
				return Double.parseDouble(m.group(1));
			}
		}
		return null;
	}

	public static String getFontfamily(String text) {
		if (text == null) {
			return null;
		}
		Pattern p = Pattern.compile("fontfamily\\=(\\w+).*");
		Vector<String> txt = Utils.decomposeStrings(text);
		for (String t : txt) {
			Matcher m = p.matcher(t);
			if (m.matches()) {
				return m.group(1);
			}
		}
		return null;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		changed_from = handler.getHelpText();
		handler.setHelpText(changed_to);
		handler.getFontHandler().setDiagramDefaultFontSize(getFontsize(changed_to));
		handler.getFontHandler().setDiagramDefaultFontFamily(getFontfamily(changed_to));
		handler.getDrawPanel().updateElements();
		handler.getDrawPanel().repaint();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		handler.setHelpText(changed_from);
		handler.getFontHandler().setDiagramDefaultFontSize(getFontsize(changed_from));
		handler.getFontHandler().setDiagramDefaultFontFamily(getFontfamily(changed_from));
		handler.getDrawPanel().updateElements();
		handler.getDrawPanel().repaint();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (c instanceof HelpPanelChanged) {
			return true;
		}
		return false;
	}

	@Override
	public Command mergeTo(Command c) {
		HelpPanelChanged tmp = (HelpPanelChanged) c;
		HelpPanelChanged ret = new HelpPanelChanged(tmp.changed_from, changed_to);
		return ret;
	}
}
