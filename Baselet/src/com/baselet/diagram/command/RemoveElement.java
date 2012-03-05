package com.baselet.diagram.command;

import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.GridElement;
import com.baselet.element.Group;


public class RemoveElement extends Command {

	private Vector<GridElement> _entities;
	private Point origin;
	private boolean _zoom;

	public RemoveElement(GridElement e) {
		this(e, true);
	}

	public RemoveElement(GridElement e, boolean zoom) {
		_entities = new Vector<GridElement>();
		_entities.add(e);
		_zoom = zoom;
		handleGroups();
	}

	public RemoveElement(Vector<GridElement> v) {
		this(v, true);
	}

	public RemoveElement(Vector<GridElement> v, boolean zoom) {
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
			handler.getDrawPanel().removeElement(e);

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

		for (GridElement e : this._entities) {
			(new AddElement(e,
					handler.realignToGrid(e.getLocation().x + offsetX),
					handler.realignToGrid(e.getLocation().y + offsetY), _zoom)).execute(handler);
		}

		handler.getDrawPanel().updatePanelAndScrollbars();
		handler.getDrawPanel().repaint();
	}
}
