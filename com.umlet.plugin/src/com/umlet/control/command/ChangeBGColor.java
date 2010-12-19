package com.umlet.control.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class ChangeBGColor extends Command {
	  
	Map<Entity,String> entities;
	JCheckBoxMenuItem cbi;
	
	public ChangeBGColor(JCheckBoxMenuItem cbi) {
		this.cbi = cbi;
	}
	  
	  public void execute(DiagramHandler handler) {
		super.execute(handler);
		if(entities == null) {
			Vector<Entity> es = handler.getSelectedEntities();
			this.entities = new HashMap<Entity,String>();
			for(Entity e : es)
				this.entities.put(e, e.getBGColorString());
		}
		for (Entity ent : this.entities.keySet()) {
			ent.setColor(cbi.getActionCommand().substring(
					"color_bgc_".length()), false);
		}
	  }
	  
	  public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (Entity ent : this.entities.keySet()) {
			ent.setColor(this.entities.get(ent), false);
		}
	  }
}
