package com.umlet.control.command;

import java.awt.Point;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.umlet.constants.Constants;
import com.umlet.control.UmletClipBoard;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;

public class Paste extends Command {

	private final static Logger log = Logger.getLogger(Paste.class);	
	
	private Point origin;
	private Vector<Entity> entities;

	private int viewpX = 0;
	private int viewpY = 0;	
	
	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		if (this.entities == null) {
			this.entities = new Vector<Entity>();
			for (Entity e : UmletClipBoard.getInstance().paste()) {		
				Entity clone = e.CloneFromMe();
				clone.assignToDiagram(handler);
				this.entities.add(clone);
			}
		}
				
		//AB: first execution of paste
		if (origin == null) {
			origin = handler.getDrawPanel().getOriginAtDefaultZoom();
			
			//AB: Include viewport position to paste on visible area
			Point viewp = handler.getDrawPanel().getScrollPanel().getViewport().getViewPosition();
			viewpX = handler.realignToGrid(false, (int) viewp.getX()) / handler.getGridSize();
			viewpY = handler.realignToGrid(false, (int) viewp.getY()) / handler.getGridSize();
		}			
		
		if (this.entities.isEmpty()) return;
		DiagramHandler.zoomEntities(Constants.DEFAULTGRIDSIZE, handler.getGridSize(), entities);
		
		// Calculate the rectangle around the copied entities
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;

		for (Entity e : this.entities) {
			minX = Math.min(e.getX(), minX);
			minY = Math.min(e.getY(), minY);
		}

		for (Entity e : this.entities) {
			e.setStickBorders(false);
			e.setInsertedAtCommand(handler.getController().getCommandCount());
			// Must use changeLocation instead of setLocation to make sure that groups are placed correctly
			e.changeLocation(
					viewpX * handler.getGridSize() - minX + handler.getGridSize() * Constants.PASTE_DISPLACEMENT_GRIDS, 
					viewpY * handler.getGridSize() - minY + handler.getGridSize() * Constants.PASTE_DISPLACEMENT_GRIDS);
		}
		
		int offsetX = origin.x - handler.getDrawPanel().getOriginAtDefaultZoom().x;
		int offsetY = origin.y - handler.getDrawPanel().getOriginAtDefaultZoom().y;
		
		offsetX = offsetX * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		offsetY = offsetY * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
	
		for (Entity e : this.entities) {
			(new AddEntity(e,
					handler.realignToGrid(e.getX() + offsetX),
					handler.realignToGrid(e.getY() + offsetY),false)).execute(handler);
		}

		handler.getDrawPanel().getSelector().deselectAll();
		for (Entity e : this.entities)
			handler.getDrawPanel().getSelector().select(e);
		
		handler.getDrawPanel().updatePanelAndScrollbars();

	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, entities);		
		(new RemoveElement(this.entities, false)).execute(handler);
		
		handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
