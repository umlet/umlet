package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.umlet.element.ActivityDiagramText;

public class GoTo {

	private Graphics2D graphics;
	private String to_id;
	private Element from_element;
	private Element to_element;
	private Direction dir;

	public GoTo(Graphics2D g, Element from, String to) {
		this.graphics = g;
		this.to_id = to;
		this.from_element = from;
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public Element getFromElement() {
		return this.from_element;
	}

	public Element getToElement() {
		return this.to_element;
	}

	public String getToElementId() {
		return this.to_id;
	}

	public void setToElement(Element e) {
		this.to_element = e;
	}

	public void paint(ActivityDiagramText dia) {
		if (this.from_element.connectOut_overrideable() && this.to_element.connectIn()
				&& this.to_element.arrowIn()) {
			Point from = this.from_element.getNonStdConnectOut(dir);
			Point to = this.to_element.getNonStdConnectIn(dir);
			Point to_origin = this.to_element.getConnect(dir);

			int x = dia.getGotoPosition(dir);
			this.graphics.drawLine(from.x, from.y, x, from.y);
			this.graphics.drawLine(x, from.y, x, to.y);

			if ((to.x == to_origin.x) && (to.y == to_origin.y)) {
				float zoom = dia.getHandler().getZoomFactor();
				Connector.drawArrow(this.graphics, zoom, x, to.y, to.x, to.y);
			}
			else this.graphics.drawLine(x, to.y, to.x, to.y);
		}
	}
}
