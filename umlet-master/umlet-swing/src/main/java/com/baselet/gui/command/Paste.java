package com.baselet.gui.command;

import java.awt.Point;
import java.util.Vector;

import com.baselet.control.constants.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.GridElement;

public class Paste extends Command {

	private Point origin;
	private Vector<GridElement> entities;

	private int viewpX = 0;
	private int viewpY = 0;

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		if (entities == null) {
			entities = new Vector<GridElement>();
			for (GridElement e : ClipBoard.getInstance().paste()) {
				GridElement clone = ElementFactorySwing.createCopy(e, handler);
				entities.add(clone);
			}
			Selector.replaceGroupsWithNewGroups(entities, handler.getDrawPanel().getSelector());
		}

		// AB: first execution of paste
		if (origin == null) {
			origin = handler.getDrawPanel().getOriginAtDefaultZoom();

			// AB: Include viewport position to paste on visible area
			Point viewp = handler.getDrawPanel().getScrollPane().getViewport().getViewPosition();
			viewpX = handler.realignToGrid(false, (int) viewp.getX()) / handler.getGridSize();
			viewpY = handler.realignToGrid(false, (int) viewp.getY()) / handler.getGridSize();
		}

		if (entities.isEmpty()) {
			return;
		}
		DiagramHandler.zoomEntities(Constants.DEFAULTGRIDSIZE, handler.getGridSize(), entities);

		// Calculate the rectangle around the copied entities
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;

		for (GridElement e : entities) {
			minX = Math.min(e.getRectangle().x, minX);
			minY = Math.min(e.getRectangle().y, minY);
		}

		for (GridElement e : entities) {
			e.setLocationDifference(
					viewpX * handler.getGridSize() - minX + handler.getGridSize() * Constants.PASTE_DISPLACEMENT_GRIDS,
					viewpY * handler.getGridSize() - minY + handler.getGridSize() * Constants.PASTE_DISPLACEMENT_GRIDS);
		}

		int offsetX = origin.x - handler.getDrawPanel().getOriginAtDefaultZoom().x;
		int offsetY = origin.y - handler.getDrawPanel().getOriginAtDefaultZoom().y;

		offsetX = offsetX * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		offsetY = offsetY * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;

		for (GridElement e : entities) {
			new AddElement(e,
					handler.realignToGrid(e.getRectangle().x + offsetX),
					handler.realignToGrid(e.getRectangle().y + offsetY), false).execute(handler);
		}

		handler.getDrawPanel().getSelector().deselectAll();
		for (GridElement e : entities) {
			handler.getDrawPanel().getSelector().select(e);
		}

		handler.getDrawPanel().updatePanelAndScrollbars();

	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, entities);
		new RemoveElement(entities, false).execute(handler);

		handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
