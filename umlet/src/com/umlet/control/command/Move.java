// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import org.apache.log4j.Logger;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class Move extends Command {

	private static Logger log = Logger.getLogger(Move.class);
	private Entity entity;

	private int _x, _y;

	public Entity getEntity() {
		return entity;
	}

	private int getX() {
		int zoomedX = _x * entity.getHandler().getGridSize();
		log.debug("Zoomed x: " + zoomedX);
		return zoomedX;
	}

	private int getY() {
		int zoomedY = _y * entity.getHandler().getGridSize();
		log.debug("Zoomed y: " + zoomedY);
		return zoomedY;
	}

	public Move(Entity e, int x, int y) {
		entity = e;
		_x = x / e.getHandler().getGridSize();
		_y = y / e.getHandler().getGridSize();
		log.debug("Base for (x,y): (" + _x + "," + _y + ")");
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		this.entity.changeLocation(getX(), getY());
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		this.entity.changeLocation(-getX(), -getY());
		Umlet.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Move)) return false;
		Move m = (Move) c;
		return this.entity == m.entity;
	}

	@Override
	public Command mergeTo(Command c) {
		Move m = (Move) c;
		Move ret = new Move(this.entity, this.getX() + m.getX(), this.getY() + m.getY());
		return ret;
	}
}
