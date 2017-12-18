package com.baselet.gui.command;

import java.awt.Color;
import java.awt.Point;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.SelectorOld;
import com.baselet.element.interfaces.GridElement;

public class Search extends Command {

	static Color _failed = new Color(227, 127, 127);
	static Color _success = new Color(148, 172, 251);
	private String regex;
	private Pattern pattern;

	public Search(String regex) {
		this.regex = ".*(" + regex.toLowerCase(Locale.ENGLISH) + ").*";
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		SelectorOld s = handler.getDrawPanel().getSelector();
		s.deselectAll();
		DrawPanel d = handler.getDrawPanel();
		pattern = Pattern.compile(regex);
		Matcher m;
		for (GridElement e : d.getGridElements()) {
			m = pattern.matcher(e.getPanelAttributes().toLowerCase(Locale.ENGLISH));
			if (m.find()) {
				s.select(e);
			}
		}

		if (s.getSelectedElements().size() == 0) {
			return;
		}

		Rectangle panelview = Converter.convert(d.getVisibleRect());
		Point p = null;
		for (GridElement e : s.getSelectedElements()) {
			if (panelview.contains(e.getRectangle())) {
				p = new Point(0, 0);
				break;
			}
			else if (p == null) {
				p = new Point(e.getRectangle().x - panelview.x - 10, e.getRectangle().y - panelview.y - 10);
			}
		}

		if (p != null) {
			d.changeViewPosition(p.x, p.y);
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		handler.getDrawPanel().getSelector().deselectAll();
	}

}
