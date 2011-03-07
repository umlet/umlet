package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public class EventRecieve extends Activity {

	private int width = (int) (15 * getZoom());

	public EventRecieve(DiagramHandler handler, Graphics2D g, String label, String id) {
		super(handler, label, g, id == null ? label : id);
		this.setLeftWidth(width);
	}

	@Override
	public boolean connectIn() {
		return false;
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {
		if (dir.equals(Direction.LEFT)) {
			Point c = this.getConnect(Direction.BOTTOM);
			Point c2 = this.getConnect(Direction.LEFT);
			c.x = c2.x + width;
			this.getGraphics().drawLine(c.x, c.y, c.x, c.y + (int) (3 * getZoom()));
			c.y += (int) (3 * getZoom());
			return c;
		}
		else return this.getConnect(dir);
	}

	@Override
	public void paint() {
		int bh = this.getHeight() / 2;
		Point cord = this.getPosition();
		int uh = cord.y - (this.getHeight() - bh);
		bh += cord.y;
		this.label.paint();
		int[] xPoints = { cord.x - this.getLeftWidth(), cord.x + this.getRightWidth(),
				cord.x + this.getRightWidth(), cord.x - this.getLeftWidth(),
				cord.x - this.getLeftWidth() + width };
		int[] yPoints = { uh, uh, bh, bh, cord.y };
		this.getGraphics().drawPolygon(xPoints, yPoints, xPoints.length);
	}
}
