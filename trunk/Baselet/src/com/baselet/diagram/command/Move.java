package com.baselet.diagram.command;


import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.Main;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.baselet.elementnew.element.uml.relation.Relation;

public class Move extends Command {
	
	private static final Logger log = Logger.getLogger(Move.class);
	
	private GridElement entity;

	private int _x, _y;

	private int _xbeforeDrag, _ybeforeDrag;

	private boolean firstDrag;

	private boolean useSetLocation;

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

	public Move(GridElement e, int x, int y, Point mousePosBeforeDrag, boolean firstDrag, boolean useSetLocation) {
		entity = e;
		int gridSize = Main.getHandlerForElement(e).getGridSize();
		_x = x / gridSize;
		_y = y / gridSize;
		_xbeforeDrag = mousePosBeforeDrag.getX() / gridSize;
		_ybeforeDrag = mousePosBeforeDrag.getY() / gridSize;
		this.firstDrag = firstDrag;
		this.useSetLocation = useSetLocation;
		log.debug("Base for (x,y): (" + _x + "," + _y + ")");
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (useSetLocation) {
			this.entity.setLocationDifference(getX(), getY(), firstDrag, getStickables());
		} else {
			// resize directions is empty and shift-key is always false, because standalone UMLet has a separate Resize-Command
			this.entity.drag(Collections.<Direction> emptySet(), getX(), getY(), getMousePosBeforeDrag(), false, firstDrag, getStickables());
		}
	}

	private List<Relation> getStickables() {
		return Main.getHandlerForElement(entity).getDrawPanel().getNewRelations();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		if (useSetLocation) {
			this.entity.setLocationDifference(-getX(), -getY(), firstDrag, getStickables());
		} else {
		this.entity.drag(Collections.<Direction> emptySet(), -getX(), -getY(), getMousePosBeforeDrag(), false, firstDrag, getStickables());
		}
		this.entity.dragEnd();
		Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Move)) return false;
		Move m = (Move) c;
		// if the mousePosBeforeDrag is different, the user has released the mousebutton inbetween and the commands should not be merged
		if (!this.getMousePosBeforeDrag().equals(m.getMousePosBeforeDrag()) || this.firstDrag != m.firstDrag || this.useSetLocation != m.useSetLocation) return false;
		return this.entity == m.entity;
	}

	@Override
	public Command mergeTo(Command c) {
		Move m = (Move) c;
		Move ret = new Move(this.entity, this.getX() + m.getX(), this.getY() + m.getY(), getMousePosBeforeDrag(), firstDrag, useSetLocation);
		return ret;
	}
}
