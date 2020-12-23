package com.baselet.gui.command;

import com.baselet.control.constants.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.GridElement;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Paste extends Command {

	private Point origin;
	private Vector<GridElement> entities;

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

		Point location = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(location, handler.getDrawPanel());
		int diffX = location.x - minX;
		int diffY = location.y - minY;

		for (GridElement e : entities) {
			e.setLocationDifference(diffX, diffY);
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
