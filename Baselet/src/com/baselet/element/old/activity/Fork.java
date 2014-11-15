package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
import com.baselet.diagram.DiagramHandler;

public class Fork extends StartElement {

	private int h = (int) (4 * getZoom());
	// pad for the connectors
	private int pad = (int) (20 * getZoom());
	private int w = (int) (80 * getZoom());

	// padding between connectors
	private int con_pad = (int) (10 * getZoom());

	private Integer current_left_x;
	private Integer current_right_x;

	public Fork(DiagramHandler handler, Graphics2D g, String id) {
		super(handler, g, (int) (Const.PAD * handler.getZoomFactor()), id == null ? "Fork" : id);
		setHeight(h + pad);
		setWidth(w);
	}

	@Override
	public void paint() {
		int x = getPosition().x;
		int y = getPosition().y;
		getGraphics().fillRect(x - w / 2, y - (h + pad) / 2, w, (int) (4 * getZoom()));
	}

	@Override
	public boolean arrowIn() {
		return true;
	}

	@Override
	protected Point getNonStdConnectIn(Direction dir) {
		Point c = getConnect(Direction.UP);

		if (dir.equals(Direction.LEFT)) {
			c.x -= (int) (10 * getZoom());
		}
		else if (dir.equals(Direction.RIGHT)) {
			c.x += (int) (10 * getZoom());
		}

		if (arrowIn()) {
			Connector.drawArrow(getGraphics(), getZoom(), c.x, c.y - (int) (10 * getZoom()), c.x, c.y);
		}
		else {
			getGraphics().drawLine(c.x, c.y - (int) (10 * getZoom()), c.x, c.y);
		}
		c.y -= (int) (10 * getZoom());
		return c;
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {
		Point c = getConnect(dir);
		if (dir.equals(Direction.LEFT)) {
			getGraphics().drawLine(c.x - (int) (10 * getZoom()), c.y + (int) (3 * getZoom()), c.x, c.y);
			c.x -= (int) (10 * getZoom());
			c.y += (int) (3 * getZoom());
		}
		else if (dir.equals(Direction.RIGHT)) {
			getGraphics().drawLine(c.x + (int) (10 * getZoom()), c.y + (int) (3 * getZoom()), c.x, c.y);
			c.x += (int) (10 * getZoom());
			c.y += (int) (3 * getZoom());
		}

		return c;
	}

	@Override
	protected Point getConnect(Direction dir) {
		Point c = (Point) getPosition().clone();

		if (current_left_x == null) {
			current_left_x = getPosition().x;
			current_right_x = current_left_x;
		}

		if (dir == Direction.UP) {
			c.y -= (pad + h) / 2;
		}
		else if (dir == Direction.DOWN) {
			c.y += (h - pad) / 2;
		}
		else if (dir == Direction.LEFT) {
			if (c.x - current_left_x < w / 2 - con_pad) {
				current_left_x -= con_pad;
			}
			c.x = current_left_x;
			c.y += (h - pad) / 2;
		}
		else if (dir == Direction.RIGHT) {
			if (current_right_x - c.x < w / 2 - con_pad) {
				current_right_x += con_pad;
			}
			c.x = current_right_x;
			c.y += (h - pad) / 2;
		}
		return c;
	}

	@Override
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
					getGraphics().drawLine(from.x, from.y, to.x, to.y - (int) (Const.PAD * getZoom()) * 2);
					from.x = to.x;
					from.y = (int) (to.y - Const.PAD * getZoom() * 2);
				}

				if (arrowOut() && e.arrowIn()) {
					Connector.drawArrow(getGraphics(), getZoom(), from.x, from.y, to.x, to.y);
				}
				else {
					getGraphics().drawLine(from.x, from.y, to.x, to.y);
				}
			}
		}
	}
}
