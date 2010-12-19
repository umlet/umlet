package com.umlet.control.command;

import java.awt.Point;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.umlet.constants.Constants;
import com.umlet.control.UmletClipBoard;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class Cut extends Command {

	private final static Logger log = Logger.getLogger(Cut.class);	
	
	private Vector<Entity> entities;
	private Point origin;

	public Cut() {

	}

	@Override
	public void execute(DiagramHandler handler) {

		super.execute(handler);
		if (this.entities == null) {
			this.entities = new Vector<Entity>();
			this.entities.addAll(handler.getDrawPanel().getSelector().getSelectedEntities());
		}

		if (this.entities.isEmpty()) return;

		// AB: clipboard copy scales the entities to 100%, so we don't have to do it manually
		UmletClipBoard.getInstance().copy(this.entities, handler);
		(new RemoveElement(this.entities, false)).execute(handler);
		
		// AB: copy origin and zoom it to 100%
		origin = handler.getDrawPanel().getOriginAtDefaultZoom();
		
		handler.getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);

		// We must zoom to the defaultGridsize before execution
		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		int offsetX = origin.x - handler.getDrawPanel().getOriginAtDefaultZoom().x;
		int offsetY = origin.y - handler.getDrawPanel().getOriginAtDefaultZoom().y;

		for (Entity e : this.entities) {
			(new AddEntity(e, handler.realignToGrid(true, e.getX() + offsetX), handler.realignToGrid(true, e.getY() + offsetY))).execute(handler);
		}
		handler.getDrawPanel().repaint();

		// And zoom back to the oldGridsize after execution
		handler.setGridAndZoom(oldZoom, false);
		
		handler.getDrawPanel().getSelector().select(entities); // undo selects the cutted entities
		
		handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
