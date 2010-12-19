// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import java.awt.Point;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;

public class RemoveElement extends Command {
	
	private Vector<Entity> _entities;
	private Point origin;
	private boolean _zoom;

	public RemoveElement(Entity e) {
		this(e, true);
	}

	public RemoveElement(Entity e, boolean zoom) {
		_entities = new Vector<Entity>();
		_entities.add(e);
		_zoom = zoom;
		handleGroups();
	}

	public RemoveElement(Vector<Entity> v) {
		this(v, true);
	}
	
	public RemoveElement(Vector<Entity> v, boolean zoom) {
		_entities = new Vector<Entity>();
		_entities.addAll(v);
		_zoom = zoom;
		handleGroups();
	}
	
	private void handleGroups() {
		for (int i = 0; i < _entities.size(); i++) {
			Entity e = _entities.elementAt(i);
			if (e instanceof Group) {
				Group g = (Group) e;
				Vector<Entity> groupElements = g.getMembers();
				_entities.addAll(groupElements);
			}
		}
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (this._entities.size() == 0) return;
		
		DrawPanel p = handler.getDrawPanel();
		for (Entity e : this._entities)
			handler.getDrawPanel().remove(e);

		origin = handler.getDrawPanel().getOriginAtDefaultZoom();
		if (_zoom) DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, _entities);
		
		p.updatePanelAndScrollbars();
		p.repaint();
		p.getSelector().deselectAll();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		
		if (_zoom) DiagramHandler.zoomEntities(Constants.DEFAULTGRIDSIZE, handler.getGridSize(), _entities);
		
		int offsetX = origin.x - handler.getDrawPanel().getOriginAtDefaultZoom().x;
		int offsetY = origin.y - handler.getDrawPanel().getOriginAtDefaultZoom().y;
		
		offsetX = offsetX * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		offsetY = offsetY * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		
		for (Entity e : this._entities) {
			(new AddEntity(e,
					handler.realignToGrid(e.getX() + offsetX),
					handler.realignToGrid(e.getY() + offsetY),_zoom)).execute(handler);
		}

		handler.getDrawPanel().updatePanelAndScrollbars();
		handler.getDrawPanel().repaint();
	}
}
