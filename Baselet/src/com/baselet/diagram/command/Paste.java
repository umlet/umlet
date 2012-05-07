package com.baselet.diagram.command;

import java.awt.Point;
import java.util.Vector;

import com.baselet.control.ClipBoard;
import com.baselet.control.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;

public class Paste extends Command {

	private Point origin;
	private Vector<GridElement> entities;

	private int viewpX = 0;
	private int viewpY = 0;

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		if (this.entities == null) {
			this.entities = new Vector<GridElement>();
			for (GridElement e : ClipBoard.getInstance().paste()) {
				GridElement clone = e.CloneFromMe();
				clone.setHandler(handler);
				this.entities.add(clone);
			}
		}

		// AB: first execution of paste
		if (origin == null) {
			origin = handler.getDrawPanel().getOriginAtDefaultZoom();

			// AB: Include viewport position to paste on visible area
			Point viewp = handler.getDrawPanel().getScrollPane().getViewport().getViewPosition();
			viewpX = handler.realignToGrid(false, (int) viewp.getX()) / handler.getGridSize();
			viewpY = handler.realignToGrid(false, (int) viewp.getY()) / handler.getGridSize();
		}

		if (this.entities.isEmpty()) return;
		DiagramHandler.zoomEntities(Constants.DEFAULTGRIDSIZE, handler.getGridSize(), entities);

		// Calculate the rectangle around the copied entities
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;

		for (GridElement e : this.entities) {
			minX = Math.min(e.getX(), minX);
			minY = Math.min(e.getY(), minY);
		}

		for (GridElement e : this.entities) {
			e.setStickingBorderActive(false);
			// Must use changeLocation instead of setLocation to make sure that groups are placed correctly
			e.changeLocation(
					viewpX * handler.getGridSize() - minX + handler.getGridSize() * Constants.PASTE_DISPLACEMENT_GRIDS,
					viewpY * handler.getGridSize() - minY + handler.getGridSize() * Constants.PASTE_DISPLACEMENT_GRIDS);
		}

		int offsetX = origin.x - handler.getDrawPanel().getOriginAtDefaultZoom().x;
		int offsetY = origin.y - handler.getDrawPanel().getOriginAtDefaultZoom().y;

		offsetX = offsetX * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		offsetY = offsetY * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;

		for (GridElement e : this.entities) {
			(new AddElement(e,
					handler.realignToGrid(e.getX() + offsetX),
					handler.realignToGrid(e.getY() + offsetY), false)).execute(handler);
		}

		handler.getDrawPanel().getSelector().deselectAll();
		for (GridElement e : this.entities)
			handler.getDrawPanel().getSelector().select(e);

		handler.getDrawPanel().updatePanelAndScrollbars();

	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, entities);
		(new RemoveElement(this.entities, false)).execute(handler);

		handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
