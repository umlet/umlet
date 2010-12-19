package com.umlet.control.command;

import java.awt.Point;
import java.util.Vector;

import com.umlet.control.Umlet;
import com.umlet.control.UmletClipBoard;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class Paste extends Command {
	
	Vector<Entity> entities;
	
	public Paste() {
		
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if(this.entities == null) {
			this.entities = new Vector<Entity>();
			for(Entity e : UmletClipBoard.getInstance().paste())
				this.entities.add(e.CloneFromMe());
		}
		
		if(this.entities.isEmpty())
			return;
		
		
		Integer x=null,y=null;
		if(handler.equals(UmletClipBoard.getInstance().copiedFrom()))
		{
			x=0; 
			y=0;
		}
		else
		{
			//calculate left top corner to insert the elements at the left top corner of the screen.
			for(Entity e : this.entities)
			{
				if(x == null) {
					x = e.getX();
					y = e.getY();
				}
				else {
					x = Math.min(e.getX(), x);
					y = Math.min(e.getY(), y);
				}
			}
		}
		
		Point viewp = handler.getDrawPanel().getScrollPanel().getViewport().getViewPosition();
		int MAIN_UNIT = Umlet.getInstance().getMainUnit();
		
		for(Entity e : this.entities) {
			(new AddEntity(e,
					viewp.x - (viewp.x % MAIN_UNIT)	+ MAIN_UNIT * 2 + e.getX() - x,
					viewp.y - (viewp.y % MAIN_UNIT)	+ MAIN_UNIT * 2 + e.getY() - y)).execute(handler);
		}
		
		handler.getDrawPanel().getSelector().deselectAll();
		for(Entity e : this.entities)
			handler.getDrawPanel().getSelector().select(e);
	}
	
	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		(new RemoveElement(this.entities)).execute(handler);
	}
}
