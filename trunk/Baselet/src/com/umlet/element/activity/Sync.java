package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

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
		this.setHeight(h + pad);
		this.setWidth(w);
	}

	@Override
	public void paint() {
		int x = this.getPosition().x;
		int y = this.getPosition().y;
		this.getGraphics().fillRect(x - w / 2, y + (pad - h) / 2, w, (int) (4 * getZoom()));
	}

	@Override
	public boolean arrowIn() {
		return true;
	}

	@Override
	protected Point getNonStdConnectIn(Direction dir) {
		Point c = this.getConnect(dir);
		Point c2 = (Point) c.clone();

		if (dir.equals(Direction.LEFT)) c2.x -= (int) (10 * getZoom());
		else if (dir.equals(Direction.RIGHT)) c2.x += (int) (10 * getZoom());
		c2.y -= (int) (10 * getZoom());

		if (this.arrowIn()) Connector.drawArrow(this.getGraphics(), getZoom(), c2.x, c2.y, c.x, c.y);
		else this.getGraphics().drawLine(c2.x, c2.y, c.x, c.y);
		return c2;
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {
		Point c = this.getConnect(Direction.BOTTOM);
		if (dir.equals(Direction.LEFT)) c.x -= (int) (10 * getZoom());
		else if (dir.equals(Direction.RIGHT)) c.x += (int) (10 * getZoom());

		this.getGraphics().drawLine(c.x, c.y + (int) (5 * getZoom()), c.x, c.y);
		c.y += (int) (5 * getZoom());

		return c;
	}

	@Override
	protected Point getConnect(Direction dir) {
		Point c = (Point) this.getPosition().clone();

		if (this.current_left_x == null) {
			this.current_left_x = this.getPosition().x;
			this.current_right_x = this.current_left_x;
		}

		if (dir == Direction.TOP) c.y += (pad - h) / 2;
		else if (dir == Direction.BOTTOM) c.y += (pad + h) / 2;
		else if (dir == Direction.LEFT) {
			if (c.x - this.current_left_x < w / 2 - this.con_pad) this.current_left_x -= this.con_pad;
			c.x = this.current_left_x;
			c.y += (pad - h) / 2;
		}
		else if (dir == Direction.RIGHT) {
			if (this.current_right_x - c.x < w / 2 - this.con_pad) this.current_right_x += this.con_pad;
			c.x = this.current_right_x;
			c.y += (pad - h) / 2;
		}
		return c;
	}

	@Override
	public void connectTo(Element e) {
		if (e != null) {
			if (this.connectIn() && e.connectOut()) {
				Point to = this.getPosition();
				Point from = e.getConnect(Direction.BOTTOM);
				if (from.x == to.x) to = this.getConnect(Direction.TOP);
				else if (from.x < to.x) to = this.getConnect(Direction.LEFT);
				else if (from.x > to.x) to = this.getConnect(Direction.RIGHT);

				if (from.x != to.x) {
					this.getGraphics().drawLine(from.x, from.y, from.x, to.y - (int) (Const.PAD * 2 * getZoom()));
					from.y = to.y - (int) (Const.PAD * 2 * getZoom());
				}

				if (this.arrowIn() && e.arrowOut()) Connector.drawArrow(this.getGraphics(), getZoom(), from.x, from.y, to.x, to.y);
				else this.getGraphics().drawLine(from.x, from.y, to.x, to.y);
			}
		}
	}
}
