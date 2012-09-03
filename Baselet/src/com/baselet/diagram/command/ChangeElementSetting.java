package com.baselet.diagram.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;
import com.umlet.element.experimental.SettingKey;


public class ChangeElementSetting extends Command {

	private Map<GridElement, String> entities;
	private SettingKey key;
	private String value;

	public ChangeElementSetting(SettingKey key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (this.entities == null) {
			Vector<GridElement> es = handler.getDrawPanel().getSelector().getSelectedEntities();
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
