package com.umlet.control.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JCheckBoxMenuItem;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class ChangeFGColor extends Command {
	  
	Map<Entity,String> entities;
	JCheckBoxMenuItem cbi;
	
	public ChangeFGColor(JCheckBoxMenuItem cbi) {
		this.cbi = cbi;
	}
	  
	  public void execute(DiagramHandler handler) {
		super.execute(handler);
		if(this.entities == null)
		{
			Vector<Entity> es = handler.getSelectedEntities();
			this.entities = new HashMap<Entity,String>();
			for(Entity e : es)
				this.entities.put(e, e.getFGColorString());
		}
		for (Entity ent : entities.keySet()) {
			ent.setColor(cbi.getActionCommand().substring(
					"color_fgc_".length()), true);
		}
	  }
	  
	  public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (Entity ent : entities.keySet()) {
			ent.setColor(this.entities.get(ent), true);
		}
	  }
}
