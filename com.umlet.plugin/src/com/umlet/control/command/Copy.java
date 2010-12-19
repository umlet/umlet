package com.umlet.control.command;

import java.util.Vector;

import com.umlet.control.UmletClipBoard;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class Copy extends Command {

	private Vector<Entity> entities;
	
	public Copy() {
		
	}

	@Override
	public void execute(DiagramHandler handler) {
		//dont execute super.execute() because no change of diagram is required.
		if(this.entities == null) 
		{	
			this.entities = new Vector<Entity>();
			for(Entity e : handler.getSelectedEntities())
				this.entities.add(e.CloneFromMe());
		}
		
		if(this.entities != null)
			UmletClipBoard.getInstance().copy(this.entities, handler);
	}
	
	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
	}
}
