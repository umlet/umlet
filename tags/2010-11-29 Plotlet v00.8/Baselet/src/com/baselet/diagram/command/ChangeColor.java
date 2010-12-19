package com.baselet.diagram.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;


public class ChangeColor extends Command {

	Map<GridElement, String> entities;
	String color;
	Boolean fg;

	public ChangeColor(String color, boolean fg) {
		this.color = color;
		this.fg = fg;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (this.entities == null) {
			Vector<GridElement> es = handler.getDrawPanel().getSelector().getSelectedEntities();
			this.entities = new HashMap<GridElement, String>();
			for (GridElement e : es)
				this.entities.put(e, e.getFGColorString());
		}
		for (GridElement ent : entities.keySet()) {
			ent.setColor(color, fg);
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (GridElement ent : entities.keySet()) {
			ent.setColor(this.entities.get(ent), fg);
		}
	}
}
