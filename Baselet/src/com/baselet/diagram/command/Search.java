package com.baselet.diagram.command;

import java.awt.Color;
import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.Selector;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.swing.Converter;
import com.baselet.element.GridElement;


public class Search extends Command {

	static Color _failed = new Color(227, 127, 127);
	static Color _success = new Color(148, 172, 251);
	private String regex;
	private Pattern pattern;

	public Search(String regex) {
		this.regex = ".*(" + regex.toLowerCase() + ").*";
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		Selector s = handler.getDrawPanel().getSelector();
		s.deselectAll();
		DrawPanel d = handler.getDrawPanel();
		pattern = Pattern.compile(regex);
		Matcher m;
		for (GridElement e : d.getAllEntities()) {
			m = pattern.matcher(e.getPanelAttributes().toLowerCase());
			if (m.find()) {
				while (e.isPartOfGroup())
					e = e.getGroup();
				s.select(e);
			}
		}

		if (s.getSelectedEntities().size() == 0) return;

		Rectangle panelview = Converter.convert(d.getVisibleRect());
		Point p = null;
		for (GridElement e : s.getSelectedEntities()) {
			if (panelview.contains(e.getRectangle())) {
				p = new Point(0, 0);
				break;
			}
			else if (p == null) p = new Point(e.getRectangle().x - panelview.x - 10, e.getRectangle().y - panelview.y - 10);
		}

		if (p != null) d.changeViewPosition(p.x, p.y);
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		handler.getDrawPanel().getSelector().deselectAll();
	}

}
