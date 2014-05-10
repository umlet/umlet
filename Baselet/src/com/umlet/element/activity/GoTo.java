package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.Main;
import com.baselet.control.enumerations.Direction;
import com.umlet.element.ActivityDiagramText;

public class GoTo {

	private Graphics2D graphics;
	private String to_id;
	private Element from_element;
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

	public void paint(ActivityDiagramText dia) {
		if (from_element.connectOut_overrideable() && to_element.connectIn()
			&& to_element.arrowIn()) {
			Point from = from_element.getNonStdConnectOut(dir);
			Point to = to_element.getNonStdConnectIn(dir);
			Point to_origin = to_element.getConnect(dir);

			int x = dia.getGotoPosition(dir);
			graphics.drawLine(from.x, from.y, x, from.y);
			graphics.drawLine(x, from.y, x, to.y);

			if (to.x == to_origin.x && to.y == to_origin.y) {
				float zoom = Main.getHandlerForElement(dia).getZoomFactor();
				Connector.drawArrow(graphics, zoom, x, to.y, to.x, to.y);
			}
			else {
				graphics.drawLine(x, to.y, to.x, to.y);
			}
		}
	}
}
