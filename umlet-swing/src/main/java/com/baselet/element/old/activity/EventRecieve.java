package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
import com.baselet.diagram.DiagramHandler;

public class EventRecieve extends Activity {

	private int width = (int) (15 * getZoom());

	public EventRecieve(DiagramHandler handler, Graphics2D g, String label, String id) {
		super(handler, label, g, id == null ? label : id);
		setLeftWidth(width);
	}

	@Override
	public boolean connectIn() {
		return false;
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {
		if (dir.equals(Direction.LEFT)) {
			Point c = getConnect(Direction.DOWN);
			Point c2 = getConnect(Direction.LEFT);
			c.x = c2.x + width;
			getGraphics().drawLine(c.x, c.y, c.x, c.y + (int) (3 * getZoom()));
			c.y += (int) (3 * getZoom());
			return c;
		}
		else {
			return getConnect(dir);
		}
	}

	@Override
	public void paint() {
		int bh = getHeight() / 2;
		Point cord = getPosition();
		int uh = cord.y - (getHeight() - bh);
		bh += cord.y;
		label.paint();
		int[] xPoints = { cord.x - getLeftWidth(), cord.x + getRightWidth(),
				cord.x + getRightWidth(), cord.x - getLeftWidth(),
				cord.x - getLeftWidth() + width };
		int[] yPoints = { uh, uh, bh, bh, cord.y };
		getGraphics().drawPolygon(xPoints, yPoints, xPoints.length);
	}
}
