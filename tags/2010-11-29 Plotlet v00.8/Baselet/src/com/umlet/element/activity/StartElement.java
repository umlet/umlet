package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public class StartElement extends Element {
	public StartElement(DiagramHandler handler, Graphics2D g, int padding, String id) {
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
			if (this.connectOut_overrideable() && e.connectIn()) {
				Point from = this.getPosition();
				Point to = e.getConnect(Direction.TOP);
				if (from.x == to.x) from = this.getConnect(Direction.BOTTOM);
				else if (from.x < to.x) from = this.getConnect(Direction.RIGHT);
				else if (from.x > to.x) from = this.getConnect(Direction.LEFT);

				if (from.x != to.x) this.getGraphics().drawLine(from.x, from.y, to.x, from.y);
				if (this.arrowOut() && e.arrowIn()) Connector.drawArrow(this.getGraphics(), getZoom(), to.x, from.y, to.x, to.y);
				else this.getGraphics().drawLine(to.x, from.y, to.x, to.y);
			}
		}
	}
}
