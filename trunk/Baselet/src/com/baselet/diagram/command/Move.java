package com.baselet.diagram.command;


import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

import com.baselet.control.Main;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.baselet.element.sticking.Stickable;

public class Move extends Command {
	
	private static final Logger log = Logger.getLogger(Move.class);
	
	private GridElement entity;

	private int _x, _y;

	public GridElement getEntity() {
		return entity;
	}

	private int getX() {
		int zoomedX = _x * Main.getHandlerForElement(entity).getGridSize();
		log.debug("Zoomed x: " + zoomedX);
		return zoomedX;
	}

	private int getY() {
		int zoomedY = _y * Main.getHandlerForElement(entity).getGridSize();
		log.debug("Zoomed y: " + zoomedY);
		return zoomedY;
	}

	public Move(GridElement e, int x, int y) {
		entity = e;
		_x = x / Main.getHandlerForElement(e).getGridSize();
		_y = y / Main.getHandlerForElement(e).getGridSize();
		log.debug("Base for (x,y): (" + _x + "," + _y + ")");
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		this.entity.drag(Collections.<Direction> emptySet(), getX(), getY(), new Point(0, 0), false, true, new ArrayList<Stickable>());
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		this.entity.drag(Collections.<Direction> emptySet(), -getX(), -getY(), new Point(0, 0), false, true, new ArrayList<Stickable>());
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
