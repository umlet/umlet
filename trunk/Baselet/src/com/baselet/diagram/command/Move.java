package com.baselet.diagram.command;


import java.util.Collections;
import java.util.List;

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
	
	private int x, y;

	private double xBeforeDrag, yBeforeDrag;

	private boolean firstDrag;

	private boolean useSetLocation;

	private List<? extends Stickable> stickables;

	public GridElement getEntity() {
		return entity;
	}

	private int getX() {
		int zoomedX = x * Main.getHandlerForElement(entity).getGridSize();
		log.debug("Zoomed x: " + zoomedX);
		return zoomedX;
	}

	private int getY() {
		int zoomedY = y * Main.getHandlerForElement(entity).getGridSize();
		log.debug("Zoomed y: " + zoomedY);
		return zoomedY;
	}
	
	private Point getMousePosBeforeDrag() {
		Double zoomedX = xBeforeDrag * Main.getHandlerForElement(entity).getGridSize();
		Double zoomedY = yBeforeDrag * Main.getHandlerForElement(entity).getGridSize();
		Point p = new Point((int)Math.round(zoomedX), (int)Math.round(zoomedY));
		log.debug("Zoomed point: " + p);
		return p;
	}
	
	public List<? extends Stickable> getStickables() {
		return stickables;
	}

	public Move(GridElement e, int x, int y, Point mousePosBeforeDrag, boolean firstDrag, boolean useSetLocation, List<? extends Stickable> stickables) {
		entity = e;
		int gridSize = Main.getHandlerForElement(e).getGridSize();
		this.x = x / gridSize;
		this.y = y / gridSize;
		this.xBeforeDrag = mousePosBeforeDrag.getX() * 1.0 / gridSize;
		this.yBeforeDrag = mousePosBeforeDrag.getY() * 1.0 / gridSize;
		this.firstDrag = firstDrag;
		this.useSetLocation = useSetLocation;
		this.stickables = stickables;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (useSetLocation) {
			this.entity.setLocationDifference(getX(), getY(), firstDrag, stickables);
		} else {
			// resize directions is empty and shift-key is always false, because standalone UMLet has a separate Resize-Command
			this.entity.drag(Collections.<Direction> emptySet(), getX(), getY(), getMousePosBeforeDrag(), false, firstDrag, stickables);
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		boolean firstDragUndo = true; // undo is always considered a firstdrag (to calculate stickables)
		if (useSetLocation) {
			this.entity.setLocationDifference(-getX(), -getY(), firstDragUndo, stickables);
		} else {
		this.entity.drag(Collections.<Direction> emptySet(), -getX(), -getY(), getMousePosBeforeDrag(), false, firstDragUndo, stickables);
		}
		this.entity.dragEnd();
		Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Move)) return false;
		Move m = (Move) c;
		boolean stickablesEqual = this.stickables.containsAll(m.stickables) && m.stickables.containsAll(this.stickables);
		return this.entity == m.entity && this.useSetLocation == m.useSetLocation && stickablesEqual;
	}

	@Override
	public Command mergeTo(Command c) {
		Move m = (Move) c;
		Move ret = new Move(this.entity, this.getX() + m.getX(), this.getY() + m.getY(), getMousePosBeforeDrag(), this.firstDrag || m.firstDrag, useSetLocation, stickables);
		return ret;
	}
}
