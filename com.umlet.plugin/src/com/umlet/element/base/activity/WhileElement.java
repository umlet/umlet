package com.umlet.element.base.activity;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class WhileElement extends Element {

	public WhileElement(Graphics2D g, int padding, String id) {
		super(g, padding, id);
	}

	public void connectTo(StartElement start, StopElement stop) {
		if (this.connectOut() && start.connectIn()) {
			Point from = this.getConnect(Direction.TOP);
			Point to = start.getConnect(Direction.LEFT);

			this.getGraphics().drawLine(from.x, from.y, from.x, to.y);
			if (this.arrowOut() && start.arrowIn()) Connector.drawArrow(this.getGraphics(), from.x, to.y, to.x, to.y);
			else this.getGraphics().drawLine(from.x, to.y, to.x, to.y);
		}

		if (this.connectIn() && stop.connectOut()) {
			Point to = this.getConnect(Direction.BOTTOM);
			Point from = stop.getConnect(Direction.LEFT);

			this.getGraphics().drawLine(from.x, from.y, to.x, from.y);
			if (this.arrowIn() && stop.arrowOut()) Connector.drawArrow(this.getGraphics(), to.x, from.y, to.x, to.y);
			else this.getGraphics().drawLine(to.x, from.y, to.x, to.y);
		}
	}
}
