package com.baselet.gui.command;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.enums.Direction;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.sticking.StickableMap;

public class Move extends Command {

	private static final Logger log = LoggerFactory.getLogger(Move.class);

	private final GridElement entity;

	private final int x, y;

	private final double mouseX;
	private final double mouseY;

	private final boolean isShiftKeyDown;

	private final boolean firstDrag;

	private final boolean useSetLocation;

	private final StickableMap stickables;

	private final Collection<Direction> resizeDirection;

	public GridElement getEntity() {
		return entity;
	}

	private int getX() {
		int zoomedX = x * gridSize();
		log.debug("Zoomed x: " + zoomedX);
		return zoomedX;
	}

	private int gridSize() {
		return HandlerElementMap.getHandlerForElement(entity).getGridSize();
	}

	private int getY() {
		int zoomedY = y * gridSize();
		log.debug("Zoomed y: " + zoomedY);
		return zoomedY;
	}

	public StickableMap getStickables() {
		return stickables;
	}

	public boolean isShiftKeyDown() {
		return isShiftKeyDown;
	}

	private Point getMousePosBeforeDrag() {
		Double zoomedX = mouseX * gridSize();
		Double zoomedY = mouseY * gridSize();
		Point p = new Point((int) Math.round(zoomedX), (int) Math.round(zoomedY));
		log.debug("Zoomed point: " + p);
		return p;
	}

	public Move(Collection<Direction> resizeDirection, boolean absoluteMousePos, GridElement e, int x, int y, Point mousePosBeforeDrag, boolean isShiftKeyDown, boolean firstDrag, boolean useSetLocation, StickableMap stickingStickables) {
		entity = e;
		int gridSize = HandlerElementMap.getHandlerForElement(e).getGridSize();
		this.x = x / gridSize;
		this.y = y / gridSize;
		mouseX = calcRelativePos(absoluteMousePos, mousePosBeforeDrag.getX(), entity.getRectangle().getX(), gridSize);
		mouseY = calcRelativePos(absoluteMousePos, mousePosBeforeDrag.getY(), entity.getRectangle().getY(), gridSize);
		this.isShiftKeyDown = isShiftKeyDown;
		this.firstDrag = firstDrag;
		this.useSetLocation = useSetLocation;
		stickables = stickingStickables;
		this.resizeDirection = resizeDirection;
	}

	public Move(Collection<Direction> resizeDirection, GridElement e, int x, int y, Point mousePosBeforeDrag, boolean isShiftKeyDown, boolean firstDrag, boolean useSetLocation, StickableMap stickingStickables) {
		this(resizeDirection, true, e, x, y, mousePosBeforeDrag, isShiftKeyDown, firstDrag, useSetLocation, stickingStickables);
	}

	/**
	 * Calculates the mouse position
	 * @param absoluteMousePos 	if true then the element location must be subtracted to get a relative position instead of an absolute, otherwise it's already relative
	 * @param mousePos			the absolute mouse position
	 * @param entityLocation	the location of the entity
	 * @param gridSize 			the result is divided by the gridsize because it can be (re)executed on different gridSizes (eg do on 100% zoom, change to 50% zoom and undo/redo)
	 * @return					the mouse position relative to the element, independend from gridSize
	 */
	private double calcRelativePos(boolean absoluteMousePos, int mousePos, int entityLocation, double gridSize) {
		double xCalcBase = mousePos * 1.0;
		if (absoluteMousePos) {
			xCalcBase -= entityLocation;
		}
		return xCalcBase / gridSize;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (useSetLocation) {
			entity.setRectangleDifference(getX(), getY(), 0, 0, firstDrag, stickables, true);
		}
		else {
			entity.drag(resizeDirection, getX(), getY(), getMousePosBeforeDrag(), isShiftKeyDown, firstDrag, stickables, true);
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		entity.undoDrag();
		entity.updateModelFromText();
		CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public void redo(DiagramHandler handler) {
		entity.redoDrag();
		entity.updateModelFromText();
		CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Move)) {
			return false;
		}
		Move m = (Move) c;
		boolean stickablesEquals = stickables.equalsMap(m.stickables);
		boolean shiftEquals = isShiftKeyDown == m.isShiftKeyDown;
		boolean notBothFirstDrag = !(firstDrag && m.firstDrag);
		return entity == m.entity && useSetLocation == m.useSetLocation && stickablesEquals && shiftEquals && notBothFirstDrag;
	}

	@Override
	public Command mergeTo(Command c) {
		Move m = (Move) c;
		Point mousePosBeforeDrag = firstDrag ? getMousePosBeforeDrag() : m.getMousePosBeforeDrag();
		// Important: absoluteMousePos=false, because the mousePos is already relative from the first constructor call!
		Move ret = new Move(m.resizeDirection, false, entity, getX() + m.getX(), getY() + m.getY(), mousePosBeforeDrag, isShiftKeyDown, firstDrag || m.firstDrag, useSetLocation, stickables);
		entity.mergeUndoDrag();
		return ret;
	}
}
