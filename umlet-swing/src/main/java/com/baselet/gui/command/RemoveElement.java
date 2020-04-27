package com.baselet.gui.command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.baselet.control.constants.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.interfaces.GridElement;

public class RemoveElement extends Command {

	private List<GridElement> _entities;
	private Point origin;
	private boolean _zoom;

	public RemoveElement(GridElement e) {
		this(e, true);
	}

	public RemoveElement(GridElement e, boolean zoom) {
		_entities = new ArrayList<GridElement>();
		_entities.add(e);
		_zoom = zoom;
	}

	public RemoveElement(List<GridElement> v) {
		this(v, true);
	}

	public RemoveElement(List<GridElement> v, boolean zoom) {
		_entities = new ArrayList<GridElement>();
		_entities.addAll(v);
		_zoom = zoom;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (_entities.size() == 0) {
			return;
		}

		DrawPanel p = handler.getDrawPanel();
		for (GridElement e : _entities) {
			handler.getDrawPanel().removeElement(e);
		}

		origin = handler.getDrawPanel().getOriginAtDefaultZoom();
		if (_zoom) {
			DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, _entities);
		}

		p.updatePanelAndScrollbars();
		p.repaint();
		p.getSelector().deselectAll();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);

		if (_zoom) {
			DiagramHandler.zoomEntities(Constants.DEFAULTGRIDSIZE, handler.getGridSize(), _entities);
		}

		int offsetX = origin.x - handler.getDrawPanel().getOriginAtDefaultZoom().x;
		int offsetY = origin.y - handler.getDrawPanel().getOriginAtDefaultZoom().y;

		offsetX = offsetX * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		offsetY = offsetY * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;

		for (GridElement e : _entities) {
			new AddElement(e,
					handler.realignToGrid(e.getRectangle().x + offsetX),
					handler.realignToGrid(e.getRectangle().y + offsetY), _zoom).execute(handler);
		}

		handler.getDrawPanel().updatePanelAndScrollbars();
		handler.getDrawPanel().repaint();
	}
}
