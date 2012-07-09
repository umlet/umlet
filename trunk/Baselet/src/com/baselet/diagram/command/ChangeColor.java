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
				if (fg) this.entities.put(e, e.getFGColorString());
				else this.entities.put(e, e.getBGColorString());
		}
		for (GridElement ent : entities.keySet()) {
			if (fg) ent.updateProperty("fg", color);
			else ent.updateProperty("bg", color);
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (GridElement ent : entities.keySet()) {
			String colorString = this.entities.get(ent);
			if (fg) ent.updateProperty("fg", colorString);
			else ent.updateProperty("bg", colorString);
		}
	}
}
