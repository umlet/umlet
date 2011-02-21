package com.baselet.diagram.command;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.GridElement;
import com.baselet.element.Group;

public class Align extends Command {

	private enum Edge {
		RIGHT, LEFT, TOP, BOTTOM;
	}

	private Point origin;
	private GridElement dominantEntity;
	private Vector<GridElement> entities;
	private Map<GridElement, Point> orgLocations = new HashMap<GridElement, Point>();
	private Edge edge;

	public Align(Vector<GridElement> entities, GridElement dominantEntity, String edge) {
		this.dominantEntity = dominantEntity;
		this.entities = new Vector<GridElement>(entities);
		this.edge = Edge.valueOf(edge.toUpperCase());
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if ((this.entities.size() == 0) || (dominantEntity == null)) return;

		// We must zoom to the defaultGridsize before execution
		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		// AB: determine fix points first item (the "dominantly selected" item)
		GridElement entity = dominantEntity;

		int left = entity.getX();
		int right = left + entity.getWidth();
		int top = entity.getY();
		int bottom = top + entity.getHeight();

		DrawPanel p = handler.getDrawPanel();
		for (GridElement e : this.entities) {
			int x = e.getX();
			int y = e.getY();

			switch (edge) {
				case LEFT:
					x = left;
					break;
				case RIGHT:
					x = right - e.getWidth();
					break;
				case TOP:
					y = top;
					break;
				case BOTTOM:
					y = bottom - e.getHeight();
					break;
			}

			// AB: update group members position if group has been adjusted
			if (e instanceof Group) {
				Point diff = new Point(x - e.getX(), y - e.getY());
				moveGroupMembers((Group) e, diff, handler);
			}

			orgLocations.put(e, e.getLocation());
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

			if (entity instanceof Group) {
				Point diff = new Point(orgLocation.x - entity.getX() + offsetX, orgLocation.y - entity.getY() + offsetY);
				moveGroupMembers((Group) entity, diff, handler);
			}

			entity.setLocation(handler.realignToGrid(true, orgLocation.x + offsetX), handler.realignToGrid(true, orgLocation.y + offsetY));
		}

		// And zoom back to the oldGridsize after execution
		handler.setGridAndZoom(oldZoom, false);

		handler.getDrawPanel().updatePanelAndScrollbars();
		handler.getDrawPanel().repaint();
	}

	private void moveGroupMembers(Group g, Point diff, DiagramHandler handler) {
		Vector<GridElement> members = g.getMembers();
		for (GridElement member : members) {
			member.changeLocation(handler.realignToGrid(true, diff.x), handler.realignToGrid(true, diff.y));
		}
	}
}
