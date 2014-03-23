package com.baselet.diagram.command;


import java.util.Collections;

import org.apache.log4j.Logger;

import com.baselet.control.Main;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;

public class Move extends Command {
	
	public static enum MoveType {
		FIRST_DRAG, CONTINUE_DRAG, SET_LOCATION;
	}
	
	private static final Logger log = Logger.getLogger(Move.class);
	
	private GridElement entity;

	private int _x, _y;

	private int _xbeforeDrag, _ybeforeDrag;

	private MoveType moveType;

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
	
	private Point getMousePosBeforeDrag() {
		int zoomedX = _xbeforeDrag * Main.getHandlerForElement(entity).getGridSize();
		int zoomedY = _ybeforeDrag * Main.getHandlerForElement(entity).getGridSize();
		Point p = new Point(zoomedX, zoomedY);
		log.debug("Zoomed point: " + p);
		return p;
	}

	public Move(GridElement e, int x, int y, Point mousePosBeforeDrag, MoveType moveType) {
		entity = e;
		int gridSize = Main.getHandlerForElement(e).getGridSize();
		_x = x / gridSize;
		_y = y / gridSize;
		_xbeforeDrag = mousePosBeforeDrag.getX() / gridSize;
		_ybeforeDrag = mousePosBeforeDrag.getY() / gridSize;
		this.moveType = moveType;
		log.debug("Base for (x,y): (" + _x + "," + _y + ")");
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (moveType == MoveType.SET_LOCATION) {
			this.entity.setLocationDifference(getX(), getY());
		} else {
			// resize directions is empty and shift-key is always false, because standalone UMLet has a separate Resize-Command
			this.entity.drag(Collections.<Direction> emptySet(), getX(), getY(), getMousePosBeforeDrag(), false, moveType == MoveType.FIRST_DRAG, Main.getHandlerForElement(entity).getDrawPanel().getNewRelations());
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		if (moveType == MoveType.SET_LOCATION) {
			this.entity.setLocationDifference(-getX(), -getY());
		} else {
		this.entity.drag(Collections.<Direction> emptySet(), -getX(), -getY(), getMousePosBeforeDrag(), false, moveType == MoveType.FIRST_DRAG, Main.getHandlerForElement(entity).getDrawPanel().getNewRelations());
		}
		this.entity.dragEnd();
		Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Move)) return false;
		Move m = (Move) c;
		// if the mousePosBeforeDrag is different, the user has released the mousebutton inbetween and the commands should not be merged
		if (!this.getMousePosBeforeDrag().equals(m.getMousePosBeforeDrag()) || this.moveType != m.moveType) return false;
		return this.entity == m.entity;
	}

	@Override
	public Command mergeTo(Command c) {
		Move m = (Move) c;
		Move ret = new Move(this.entity, this.getX() + m.getX(), this.getY() + m.getY(), getMousePosBeforeDrag(), moveType);
		return ret;
	}
}
