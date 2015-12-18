package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
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
			if (connectIn() && e.connectOut()) {
				Point to = getPosition();
				Point from = e.getConnect(Direction.DOWN);
				if (from.x == to.x) {
					to = getConnect(Direction.UP);
				}
				else if (from.x < to.x) {
					to = getConnect(Direction.LEFT);
				}
				else if (from.x > to.x) {
					to = getConnect(Direction.RIGHT);
				}

				if (from.x != to.x) {
					getGraphics().drawLine(from.x, from.y, from.x, to.y);
					if (arrowIn() && e.arrowOut()) {
						Connector.drawArrow(getGraphics(), getZoom(), from.x, to.y, to.x, to.y);
					}
					else {
						getGraphics().drawLine(from.x, to.y, to.x, to.y);
					}
				}
				else {
					if (arrowIn() && e.arrowOut()) {
						Connector.drawArrow(getGraphics(), getZoom(), from.x, from.y, to.x, to.y);
					}
					else {
						getGraphics().drawLine(from.x, from.y, to.x, to.y);
					}
				}
			}
		}
	}
}
