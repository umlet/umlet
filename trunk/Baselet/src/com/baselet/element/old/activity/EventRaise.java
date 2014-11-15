package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;

public class EventRaise extends Activity {

	private int width = (int) (15 * getZoom());

	public EventRaise(DiagramHandler handler, Graphics2D g, String label, String id) {
		super(handler, label, g, id == null ? label : id);
		setRightWidth(width);
	}

	@Override
	public boolean connectOut_overrideable() {
		return false;
	}

	@Override
	public void paint() {
		int bh = getHeight() / 2;
		Point cord = getPosition();
		int uh = cord.y - (getHeight() - bh);
		bh += cord.y;
		label.paint();
		int[] xPoints = { cord.x - getLeftWidth(), cord.x + getRightWidth() - width,
				cord.x + getRightWidth(), cord.x + getRightWidth() - width,
				cord.x - getLeftWidth() };
		int[] yPoints = { uh, uh, cord.y, bh, bh };
		getGraphics().drawPolygon(xPoints, yPoints, xPoints.length);
	}

}
