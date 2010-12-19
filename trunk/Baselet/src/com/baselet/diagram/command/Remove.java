package com.baselet.diagram.command;

import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.GridElement;
import com.baselet.element.Group;

public class Remove extends Command {

	private Vector<GridElement> _entities;
	private Point origin;
	private boolean _zoom;

	public Remove(GridElement e) {
		this(e, true);
	}

	public Remove(GridElement e, boolean zoom) {
		_entities = new Vector<GridElement>();
		_entities.add(e);
		_zoom = zoom;
		handleGroups();
	}

	public Remove(Vector<GridElement> v) {
		this(v, true);
	}

	public Remove(Vector<GridElement> v, boolean zoom) {
		_entities = new Vector<GridElement>();
		_entities.addAll(v);
		_zoom = zoom;
		handleGroups();
	}

	private void handleGroups() {
		for (int i = 0; i < _entities.size(); i++) {
			GridElement e = _entities.elementAt(i);
			if (e instanceof Group) {
				Group g = (Group) e;
				Vector<GridElement> groupElements = g.getMembers();
				_entities.addAll(groupElements);
			}
		}
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (this._entities.size() == 0) return;

		DrawPanel p = handler.getDrawPanel();
		for (GridElement e : this._entities)
			handler.getDrawPanel().remove(e);

		origin = handler.getDrawPanel().getOriginAtDefaultZoom();
		if (_zoom) DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, _entities);

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

		for (GridElement e : this._entities) {
			(new AddEntity(e,
					handler.realignToGrid(e.getX() + offsetX),
					handler.realignToGrid(e.getY() + offsetY), _zoom)).execute(handler);
		}
	}
}
