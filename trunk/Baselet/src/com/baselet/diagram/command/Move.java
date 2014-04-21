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

	private GridElement entity;

	private int x, y;
	
	private Point mousePosBeforeDrag;

	private boolean firstDrag;

	private boolean useSetLocation;

	private StickableMap stickables;
	
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

	public Move(GridElement e, int x, int y, Point mousePosBeforeDrag, boolean firstDrag, boolean useSetLocation, StickableMap stickingStickables) {
		entity = e;
		int gridSize = Main.getHandlerForElement(e).getGridSize();
		this.x = x / gridSize;
		this.y = y / gridSize;
		this.mousePosBeforeDrag = mousePosBeforeDrag;
		this.firstDrag = firstDrag;
		this.useSetLocation = useSetLocation;
		this.stickables = stickingStickables;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		Rectangle b = calcAtMinZoom(entity.getRectangle());
		additionalAttributesBefore = entity.getAdditionalAttributes();
		if (useSetLocation) {
			this.entity.setLocationDifference(getX(), getY(), firstDrag, stickables);
		} else {
			// resize directions is empty and shift-key is always false, because standalone UMLet has a separate Resize-Command
			this.entity.drag(Collections.<Direction> emptySet(), getX(), getY(), mousePosBeforeDrag, false, firstDrag, stickables);
		}
		Rectangle a = calcAtMinZoom(entity.getRectangle());
		boundsBefore = subtract(b, a);
	}

	private Rectangle subtract(Rectangle b, Rectangle a) {
		return new Rectangle(b.x-a.x, b.y-a.y, b.width-a.width, b.height-a.height);
	}

	private Rectangle add(Rectangle b, Rectangle a) {
		return new Rectangle(b.x+a.x, b.y+a.y, b.width+a.width, b.height+a.height);
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
		if (!(c instanceof Move)) return false;
		Move m = (Move) c;
		boolean stickablesEqual = this.stickables.equalsMap(m.stickables);
		boolean notBothFirstDrag = !(this.firstDrag && m.firstDrag);
		return this.entity == m.entity && this.useSetLocation == m.useSetLocation && stickablesEqual && notBothFirstDrag;
	}

	@Override
	public Command mergeTo(Command c) {
		Move m = (Move) c;
		Point mousePosBeforeDrag = this.firstDrag ? this.mousePosBeforeDrag : m.mousePosBeforeDrag;
		Move ret = new Move(this.entity, this.getX() + m.getX(), this.getY() + m.getY(), mousePosBeforeDrag, this.firstDrag || m.firstDrag, useSetLocation, stickables);
		ret.boundsBefore = add(this.boundsBefore, m.boundsBefore);
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
