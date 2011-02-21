package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public class StopElement extends Element {

	public StopElement(DiagramHandler handler, Graphics2D g, int padding, String id) {
		super(handler, g, padding, id);
	}

	@Override
	public void paint() {

	}

	@Override
	public boolean arrowIn() {
		return false;
	}

	public void connectTo(Element e) {
		if (e != null) {
			if (this.connectIn() && e.connectOut()) {
				Point to = this.getPosition();
				Point from = e.getConnect(Direction.BOTTOM);
				if (from.x == to.x) to = this.getConnect(Direction.TOP);
				else if (from.x < to.x) to = this.getConnect(Direction.LEFT);
				else if (from.x > to.x) to = this.getConnect(Direction.RIGHT);

				if (from.x != to.x) {
					this.getGraphics().drawLine(from.x, from.y, from.x, to.y);
					if (this.arrowIn() && e.arrowOut()) Connector.drawArrow(this.getGraphics(), getZoom(), from.x, to.y, to.x, to.y);
					else this.getGraphics().drawLine(from.x, to.y, to.x, to.y);
				}
				else {
					if (this.arrowIn() && e.arrowOut()) Connector.drawArrow(this.getGraphics(), getZoom(), from.x, from.y, to.x, to.y);
					else this.getGraphics().drawLine(from.x, from.y, to.x, to.y);
				}
			}
		}
	}
}
