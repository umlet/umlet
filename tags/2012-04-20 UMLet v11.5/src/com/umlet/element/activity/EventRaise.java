package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public class EventRaise extends Activity {

	private int width = (int) (15 * getZoom());

	public EventRaise(DiagramHandler handler, Graphics2D g, String label, String id) {
		super(handler, label, g, id == null ? label : id);
		this.setRightWidth(width);
	}

	@Override
	public boolean connectOut_overrideable() {
		return false;
	}

	@Override
	public void paint() {
		int bh = this.getHeight() / 2;
		Point cord = this.getPosition();
		int uh = cord.y - (this.getHeight() - bh);
		bh += cord.y;
		this.label.paint();
		int[] xPoints = { cord.x - this.getLeftWidth(), cord.x + this.getRightWidth() - width,
				cord.x + this.getRightWidth(), cord.x + this.getRightWidth() - width,
				cord.x - this.getLeftWidth() };
		int[] yPoints = { uh, uh, cord.y, bh, bh };
		this.getGraphics().drawPolygon(xPoints, yPoints, xPoints.length);
	}

}
