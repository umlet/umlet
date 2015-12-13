package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
import com.baselet.diagram.DiagramHandler;

public abstract class WhileElement extends Element {

	public WhileElement(DiagramHandler handler, Graphics2D g, int padding, String id) {
		super(handler, g, padding, id);
	}

	public void connectTo(StartElement start, StopElement stop) {
		if (connectOut() && start.connectIn()) {
			Point from = getConnect(Direction.UP);
			Point to = start.getConnect(Direction.LEFT);

			getGraphics().drawLine(from.x, from.y, from.x, to.y);
			if (arrowOut() && start.arrowIn()) {
				Connector.drawArrow(getGraphics(), getZoom(), from.x, to.y, to.x, to.y);
			}
			else {
				getGraphics().drawLine(from.x, to.y, to.x, to.y);
			}
		}

		if (connectIn() && stop.connectOut()) {
			Point to = getConnect(Direction.DOWN);
			Point from = stop.getConnect(Direction.LEFT);

			getGraphics().drawLine(from.x, from.y, to.x, from.y);
			if (arrowIn() && stop.arrowOut()) {
				Connector.drawArrow(getGraphics(), getZoom(), to.x, from.y, to.x, to.y);
			}
			else {
				getGraphics().drawLine(to.x, from.y, to.x, to.y);
			}
		}
	}
}
