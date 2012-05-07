package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public abstract class WhileElement extends Element {

	public WhileElement(DiagramHandler handler, Graphics2D g, int padding, String id) {
		super(handler, g, padding, id);
	}

	public void connectTo(StartElement start, StopElement stop) {
		if (this.connectOut() && start.connectIn()) {
			Point from = this.getConnect(Direction.TOP);
			Point to = start.getConnect(Direction.LEFT);

			this.getGraphics().drawLine(from.x, from.y, from.x, to.y);
			if (this.arrowOut() && start.arrowIn()) Connector.drawArrow(this.getGraphics(), getZoom(), from.x, to.y, to.x, to.y);
			else this.getGraphics().drawLine(from.x, to.y, to.x, to.y);
		}

		if (this.connectIn() && stop.connectOut()) {
			Point to = this.getConnect(Direction.BOTTOM);
			Point from = stop.getConnect(Direction.LEFT);

			this.getGraphics().drawLine(from.x, from.y, to.x, from.y);
			if (this.arrowIn() && stop.arrowOut()) Connector.drawArrow(this.getGraphics(), getZoom(), to.x, from.y, to.x, to.y);
			else this.getGraphics().drawLine(to.x, from.y, to.x, to.y);
		}
	}
}
