package com.umlet.control.command;

import java.awt.Point;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.UmletClipBoard;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class Paste extends Command {

	Vector<Entity> entities;

	public Paste() {

	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		// We must zoom to the defaultGridsize before execution
		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		if (this.entities == null) {
			this.entities = new Vector<Entity>();
			for (Entity e : UmletClipBoard.getInstance().paste())
				this.entities.add(e.CloneFromMe());
		}

		if (this.entities.isEmpty()) return;

		// Calculate the rectangle around the copied entities
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;

		for (Entity e : this.entities) {
			minX = Math.min(e.getX(), minX);
			minY = Math.min(e.getY(), minY);
			maxX = Math.max(e.getX() + e.getWidth(), maxX);
			maxY = Math.max(e.getY() + e.getHeight(), maxY);
		}

		// TODO partly implemented paste near the copied entities

		// Rectangle viewRect = handler.getDrawPanel().getScrollPanel().getViewport().getViewRect();
		// boolean insertAtTopLeft = false;
		// if ( (maxX <= viewRect.getX()) ||
		// (maxY <= viewRect.getY()) ||
		// (minX >= viewRect.getX() + viewRect.getWidth()) ||
		// (minY >= viewRect.getY() + viewRect.getHeight())) {
		// insertAtTopLeft = true;
		// }

		Point viewp = handler.getDrawPanel().getScrollPanel().getViewport().getViewPosition();
		for (Entity e : this.entities) {
			int upperLeftX, upperLeftY;
			// if (insertAtTopLeft) {
			upperLeftX = (int) (viewp.getX() - viewp.getX() % oldZoom);
			upperLeftY = (int) (viewp.getY() - viewp.getY() % oldZoom);
			// }
			// else {
			// upperLeftX = minX;
			// upperLeftY = minY;
			// }
			(new AddEntity(e,
					handler.ensureValueIsOnGrid(e.getX() - minX + upperLeftX / oldZoom * Constants.DEFAULTGRIDSIZE + Constants.PASTE_DISPLACEMENT),
					handler.ensureValueIsOnGrid(e.getY() - minY + upperLeftY / oldZoom * Constants.DEFAULTGRIDSIZE + Constants.PASTE_DISPLACEMENT))).execute(handler);
		}

		handler.getDrawPanel().getSelector().deselectAll();
		for (Entity e : this.entities)
			handler.getDrawPanel().getSelector().select(e);

		// And zoom back to the oldGridsize after execution
		handler.setGridAndZoom(oldZoom, false);

	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		(new RemoveElement(this.entities)).execute(handler);
	}
}
