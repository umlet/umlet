package com.baselet.diagram.command;

import java.util.Collections;

import org.apache.log4j.Logger;

import com.baselet.control.Main;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.sticking.StickableMap;

public class Move extends Command {

	private static final Logger log = Logger.getLogger(Move.class);

	private final GridElement entity;

	private final int x, y;

	private final double xBeforeDrag;
	private final double yBeforeDrag;

	private final boolean firstDrag;

	private final boolean useSetLocation;

	private final StickableMap stickables;

	private String additionalAttributesBefore;

	private Rectangle boundsBefore;

	public GridElement getEntity() {
		return entity;
	}

	private int getX() {
		int zoomedX = x * gridSize();
		log.debug("Zoomed x: " + zoomedX);
		return zoomedX;
	}

	private int gridSize() {
		return Main.getHandlerForElement(entity).getGridSize();
	}

	private int getY() {
		int zoomedY = y * gridSize();
		log.debug("Zoomed y: " + zoomedY);
		return zoomedY;
	}

	public StickableMap getStickables() {
		return stickables;
	}

	private Point getMousePosBeforeDrag() {
		Double zoomedX = xBeforeDrag * gridSize();
		Double zoomedY = yBeforeDrag * gridSize();
		Point p = new Point((int) Math.round(zoomedX), (int) Math.round(zoomedY));
		log.debug("Zoomed point: " + p);
		return p;
	}

	public Move(boolean absoluteMousePos, GridElement e, int x, int y, Point mousePosBeforeDrag, boolean firstDrag, boolean useSetLocation, StickableMap stickingStickables) {
		entity = e;
		int gridSize = Main.getHandlerForElement(e).getGridSize();
		this.x = x / gridSize;
		this.y = y / gridSize;
		xBeforeDrag = calcRelativePos(absoluteMousePos, mousePosBeforeDrag.getX(), entity.getRectangle().getX(), gridSize);
		yBeforeDrag = calcRelativePos(absoluteMousePos, mousePosBeforeDrag.getY(), entity.getRectangle().getY(), gridSize);
		this.firstDrag = firstDrag;
		this.useSetLocation = useSetLocation;
		stickables = stickingStickables;
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

	public Move(GridElement e, int x, int y, Point mousePosBeforeDrag, boolean firstDrag, boolean useSetLocation, StickableMap stickingStickables) {
		this(true, e, x, y, mousePosBeforeDrag, firstDrag, useSetLocation, stickingStickables);
	}

	@Override
	public void execute(DiagramHandler handler) {
		// System.out.println("DRAG " + mousePosBeforeDrag);
		super.execute(handler);
		Rectangle b = calcAtMinZoom(entity.getRectangle());
		additionalAttributesBefore = entity.getAdditionalAttributes();
		if (useSetLocation) {
			entity.setLocationDifference(getX(), getY(), firstDrag, stickables);
		}
		else {
			// resize directions is empty and shift-key is always false, because standalone UMLet has a separate Resize-Command
			entity.drag(Collections.<Direction> emptySet(), getX(), getY(), getMousePosBeforeDrag(), false, firstDrag, stickables);
		}
		Rectangle a = calcAtMinZoom(entity.getRectangle());
		boundsBefore = b.subtract(a);
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		Rectangle boundsBeforeZoomed = applyCurrentZoom(boundsBefore);
		entity.setLocationDifference(boundsBeforeZoomed.getX(), boundsBeforeZoomed.getY(), true, stickables); // use to make sure stickables are moved - TODO refactor Stickable-Implementations to use move-commands, then they would undo on their own
		entity.setAdditionalAttributes(additionalAttributesBefore);
		entity.updateModelFromText();
		Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Move)) {
			return false;
		}
		Move m = (Move) c;
		boolean stickablesEqual = stickables.equalsMap(m.stickables);
		boolean notBothFirstDrag = !(firstDrag && m.firstDrag);
		return entity == m.entity && useSetLocation == m.useSetLocation && stickablesEqual && notBothFirstDrag;
	}

	@Override
	public Command mergeTo(Command c) {
		Move m = (Move) c;
		Point mousePosBeforeDrag = firstDrag ? getMousePosBeforeDrag() : m.getMousePosBeforeDrag();
		// Important: absoluteMousePos=false, because the mousePos is already relative from the first constructor call!
		Move ret = new Move(false, entity, getX() + m.getX(), getY() + m.getY(), mousePosBeforeDrag, firstDrag || m.firstDrag, useSetLocation, stickables);
		ret.boundsBefore = boundsBefore.add(m.boundsBefore);
		ret.additionalAttributesBefore = m.additionalAttributesBefore;
		return ret;
	}

	private Rectangle calcAtMinZoom(Rectangle rectangle) {
		int xBefore = rectangle.getX() / gridSize();
		int yBefore = rectangle.getY() / gridSize();
		int wBefore = rectangle.getWidth() / gridSize();
		int hBefore = rectangle.getHeight() / gridSize();
		return new Rectangle(xBefore, yBefore, wBefore, hBefore);
	}

	private Rectangle applyCurrentZoom(Rectangle rectangle) {
		int xBefore = rectangle.getX() * gridSize();
		int yBefore = rectangle.getY() * gridSize();
		int wBefore = rectangle.getWidth() * gridSize();
		int hBefore = rectangle.getHeight() * gridSize();
		return new Rectangle(xBefore, yBefore, wBefore, hBefore);
	}
}
