package com.baselet.gui.command;

import java.awt.Point;
import java.util.Vector;

import com.baselet.control.constants.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.GridElement;

public class Cut extends Command {

	private Vector<GridElement> entities;
	private Point origin;

	public Cut() {

	}

	@Override
	public void execute(DiagramHandler handler) {

		super.execute(handler);
		if (entities == null) {
			entities = new Vector<GridElement>();
			entities.addAll(handler.getDrawPanel().getSelector().getSelectedElements());
		}

		if (entities.isEmpty()) {
			return;
		}

		// AB: clipboard copy scales the entities to 100%, so we don't have to do it manually
		ClipBoard.getInstance().copyAndZoomToDefaultLevel(entities, handler);
		new RemoveElement(entities, false).execute(handler);

		// AB: copy origin and zoom it to 100%
		origin = handler.getDrawPanel().getOriginAtDefaultZoom();

		handler.getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);

		// We must zoom to the defaultGridsize before execution
		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		int offsetX = origin.x - handler.getDrawPanel().getOriginAtDefaultZoom().x;
		int offsetY = origin.y - handler.getDrawPanel().getOriginAtDefaultZoom().y;

		for (GridElement e : entities) {
			new AddElement(e,
					handler.realignToGrid(true, e.getRectangle().x + offsetX),
					handler.realignToGrid(true, e.getRectangle().y + offsetY), false).execute(handler);
		}
		handler.getDrawPanel().repaint();

		// And zoom back to the oldGridsize after execution
		handler.setGridAndZoom(oldZoom, false);

		handler.getDrawPanel().getSelector().select(entities); // undo selects the cutted entities

		handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
