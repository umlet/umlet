package com.baselet.gui.command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.interfaces.GridElement;

public class Align extends Command {

	private enum Edge {
		RIGHT, LEFT, TOP, BOTTOM;
	}

	private Point origin;
	private GridElement dominantEntity;
	private List<GridElement> entities;
	private Map<GridElement, Point> orgLocations = new HashMap<GridElement, Point>();
	private Edge edge;

	public Align(List<GridElement> entities, GridElement dominantEntity, String edge) {
		this.dominantEntity = dominantEntity;
		this.entities = new ArrayList<GridElement>(entities);
		this.edge = Edge.valueOf(edge.toUpperCase(Locale.ENGLISH));
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (entities.size() == 0 || dominantEntity == null) {
			return;
		}

		// We must zoom to the defaultGridsize before execution
		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		// AB: determine fix points first item (the "dominantly selected" item)
		GridElement entity = dominantEntity;

		int left = entity.getRectangle().x;
		int right = left + entity.getRectangle().width;
		int top = entity.getRectangle().y;
		int bottom = top + entity.getRectangle().height;

		DrawPanel p = handler.getDrawPanel();
		for (GridElement e : entities) {
			Rectangle rectangle = e.getRectangle();
			int x = rectangle.x;
			int y = rectangle.y;

			switch (edge) {
				case LEFT:
					x = left;
					break;
				case RIGHT:
					x = right - e.getRectangle().width;
					break;
				case TOP:
					y = top;
					break;
				case BOTTOM:
					y = bottom - e.getRectangle().height;
					break;
			}

			orgLocations.put(e, new Point(rectangle.x, rectangle.y));
			e.setLocation(handler.realignToGrid(true, x), handler.realignToGrid(true, y));
		}

		// And zoom back to the oldGridsize after execution
		handler.setGridAndZoom(oldZoom, false);

		// AB: copy origin and zoom it to 100%
		origin = handler.getDrawPanel().getOriginAtDefaultZoom();

		p.updatePanelAndScrollbars();
		p.repaint();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);

		// We must zoom to the defaultGridsize before execution
		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		int offsetX = origin.x - handler.getDrawPanel().getOrigin().x;
		int offsetY = origin.y - handler.getDrawPanel().getOrigin().y;

		for (GridElement entity : entities) {

			Point orgLocation = orgLocations.get(entity);

			entity.setLocation(handler.realignToGrid(true, orgLocation.x + offsetX), handler.realignToGrid(true, orgLocation.y + offsetY));
		}

		// And zoom back to the oldGridsize after execution
		handler.setGridAndZoom(oldZoom, false);

		handler.getDrawPanel().updatePanelAndScrollbars();
		handler.getDrawPanel().repaint();
	}
}
