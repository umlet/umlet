package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;

public class Connector {

	private Element e1, e2;
	private Graphics2D graphics;

	public static void drawArrow(Graphics2D graphics, float zoom, int x1, int y1, int x2, int y2) {

		double ax1, ax2, ay1, ay2;
		int ax_1, ax_2, ay_1, ay_2;
		double angle = Math.atan2(y2 - y1, x2 - x1);
		ax1 = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) - (int) (7 * zoom);
		ax2 = ax1;
		ay1 = -(int) (4 * zoom);
		ay2 = (int) (5 * zoom);

		ax_1 = (int) (Math.cos(angle) * ax1 - Math.sin(angle) * ay1) + x1;
		ay_1 = (int) (Math.sin(angle) * ax1 + Math.cos(angle) * ay1) + y1;
		ax_2 = (int) (Math.cos(angle) * ax2 - Math.sin(angle) * ay2) + x1;
		ay_2 = (int) (Math.sin(angle) * ax2 + Math.cos(angle) * ay2) + y1;

		graphics.drawLine(x1, y1, x2, y2);
		graphics.drawLine(x2, y2, ax_1, ay_1);
		graphics.drawLine(x2, y2, ax_2, ay_2);
	}

	public Connector(Graphics2D g, Element e1, Element e2) {
		graphics = g;
		this.e1 = e1;
		this.e2 = e2;
	}

	public void paint() {
		if (e1.connectOut() && e2.connectIn()) {
			Point c2 = e2.getConnect(Direction.UP);
			Point c1 = e1.getConnect(Direction.DOWN);

			if (c1 != null && c2 != null) {
				if (!c1.equals(c2)) {
					if (e1.arrowOut() && e2.arrowIn()) {
						float zoom = e1.getHandler().getZoomFactor();
						Connector.drawArrow(graphics, zoom, c1.x, c1.y, c2.x, c2.y);
					}
					else {
						graphics.drawLine(c1.x, c1.y, c2.x, c2.y);
					}
				}
			}
		}
	}
}
