package com.baselet.diagram.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;


public class ChangeElementSetting extends Command {

	private Map<GridElement, String> entities;
	private String key;
	private String value;

	public ChangeElementSetting(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (this.entities == null) {
			List<GridElement> es = handler.getDrawPanel().getSelector().getSelectedElements();
			this.entities = new HashMap<GridElement, String>();
			for (GridElement e : es)
				this.entities.put(e, e.getSetting(key));
		}
		for (GridElement ent : entities.keySet()) {
			ent.updateProperty(key, value);
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (GridElement ent : entities.keySet()) {
			ent.updateProperty(key, this.entities.get(ent));
		}
	}
}
