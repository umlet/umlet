package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
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
			if (connectOut_overrideable() && e.connectIn()) {
				Point from = getPosition();
				Point to = e.getConnect(Direction.UP);
				if (from.x == to.x) {
					from = getConnect(Direction.DOWN);
				}
				else if (from.x < to.x) {
					from = getConnect(Direction.RIGHT);
				}
				else if (from.x > to.x) {
					from = getConnect(Direction.LEFT);
				}

				if (from.x != to.x) {
					getGraphics().drawLine(from.x, from.y, to.x, from.y);
				}
				if (arrowOut() && e.arrowIn()) {
					Connector.drawArrow(getGraphics(), getZoom(), to.x, from.y, to.x, to.y);
				}
				else {
					getGraphics().drawLine(to.x, from.y, to.x, to.y);
				}
			}
		}
	}
}
