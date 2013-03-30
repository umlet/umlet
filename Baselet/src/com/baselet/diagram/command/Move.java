package com.baselet.diagram.command;


import org.apache.log4j.Logger;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;

public class Move extends Command {

	private final static Logger log = Logger.getLogger(Utils.getClassName());
	private GridElement entity;

	private int _x, _y;

	public GridElement getEntity() {
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

	public Move(GridElement e, int x, int y) {
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
		Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
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
