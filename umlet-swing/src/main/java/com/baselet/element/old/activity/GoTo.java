package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;

public class GoTo {

	private final Graphics2D graphics;
	private final String to_id;
	private final Element from_element;
	private Element to_element;
	private Direction dir;

	public GoTo(Graphics2D g, Element from, String to) {
		graphics = g;
		to_id = to;
		from_element = from;
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public Direction getDirection() {
		return dir;
	}

	public Element getFromElement() {
		return from_element;
	}

	public Element getToElement() {
		return to_element;
	}

	public String getToElementId() {
		return to_id;
	}

	public void setToElement(Element e) {
		to_element = e;
	}

	public void paint(float zoomFactor, int gotoPosition) {
		if (from_element.connectOut_overrideable() && to_element.connectIn() && to_element.arrowIn()) {
			Point from = from_element.getNonStdConnectOut(dir);
			Point to = to_element.getNonStdConnectIn(dir);
			Point to_origin = to_element.getConnect(dir);

			int x = gotoPosition;
			graphics.drawLine(from.x, from.y, x, from.y);
			graphics.drawLine(x, from.y, x, to.y);

			if (to.x == to_origin.x && to.y == to_origin.y) {
				float zoom = zoomFactor;
				Connector.drawArrow(graphics, zoom, x, to.y, to.x, to.y);
			}
			else {
				graphics.drawLine(x, to.y, to.x, to.y);
			}
		}
	}
}
