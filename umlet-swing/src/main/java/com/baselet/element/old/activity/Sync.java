package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
import com.baselet.diagram.DiagramHandler;

public class Sync extends StopElement {

	private int h = (int) (4 * getZoom());
	// pad for the connectors
	private int pad = (int) (20 * getZoom());
	private int w = (int) (80 * getZoom());

	// padding between connectors
	private int con_pad = (int) (10 * getZoom());

	private Integer current_left_x;
	private Integer current_right_x;

	public Sync(DiagramHandler handler, Graphics2D g, String id) {
		super(handler, g, (int) (Const.PAD * 2 * handler.getZoomFactor()), id == null ? "Sync" : id);
		setHeight(h + pad);
		setWidth(w);
	}

	@Override
	public void paint() {
		int x = getPosition().x;
		int y = getPosition().y;
		getGraphics().fillRect(x - w / 2, y + (pad - h) / 2, w, (int) (4 * getZoom()));
	}

	@Override
	public boolean arrowIn() {
		return true;
	}

	@Override
	protected Point getNonStdConnectIn(Direction dir) {
		Point c = getConnect(dir);
		Point c2 = (Point) c.clone();

		if (dir.equals(Direction.LEFT)) {
			c2.x -= (int) (10 * getZoom());
		}
		else if (dir.equals(Direction.RIGHT)) {
			c2.x += (int) (10 * getZoom());
		}
		c2.y -= (int) (10 * getZoom());

		if (arrowIn()) {
			Connector.drawArrow(getGraphics(), getZoom(), c2.x, c2.y, c.x, c.y);
		}
		else {
			getGraphics().drawLine(c2.x, c2.y, c.x, c.y);
		}
		return c2;
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {
		Point c = getConnect(Direction.DOWN);
		if (dir.equals(Direction.LEFT)) {
			c.x -= (int) (10 * getZoom());
		}
		else if (dir.equals(Direction.RIGHT)) {
			c.x += (int) (10 * getZoom());
		}

		getGraphics().drawLine(c.x, c.y + (int) (5 * getZoom()), c.x, c.y);
		c.y += (int) (5 * getZoom());

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
			c.y += (pad - h) / 2;
		}
		else if (dir == Direction.DOWN) {
			c.y += (pad + h) / 2;
		}
		else if (dir == Direction.LEFT) {
			if (c.x - current_left_x < w / 2 - con_pad) {
				current_left_x -= con_pad;
			}
			c.x = current_left_x;
			c.y += (pad - h) / 2;
		}
		else if (dir == Direction.RIGHT) {
			if (current_right_x - c.x < w / 2 - con_pad) {
				current_right_x += con_pad;
			}
			c.x = current_right_x;
			c.y += (pad - h) / 2;
		}
		return c;
	}

	@Override
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
					getGraphics().drawLine(from.x, from.y, from.x, to.y - (int) (Const.PAD * 2 * getZoom()));
					from.y = to.y - (int) (Const.PAD * 2 * getZoom());
				}

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
