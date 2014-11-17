package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;

public class PartActivity extends Activity {

	private int minwidth = (int) (120 * getZoom());
	private int height = (int) (20 * getZoom());

	public PartActivity(DiagramHandler handler, String label, Graphics2D g, String id) {
		super(handler, label, g, id == null ? label : id);
		setHeight(height);
	}

	@Override
	protected int getLeftWidth() {
		return super.getLeftWidth() > minwidth / 2 ? super.getLeftWidth() : minwidth / 2;
	}

	@Override
	protected int getRightWidth() {
		return super.getRightWidth() > minwidth / 2 ? super.getRightWidth() : minwidth / 2;
	}

	@Override
	public void paint() {
		Point cord = getPosition();
		int h = getHeight();
		label.paint();
		getGraphics().drawRoundRect(cord.x - getLeftWidth(), cord.y - h / 2, getWidth(), h, (int) (20 * getZoom()), (int) (20 * getZoom()));
		getGraphics().drawRoundRect(cord.x - minwidth / 4, cord.y + h / 2 - height + (int) (5 * getZoom()), (int) (30 * getZoom()), (int) (10 * getZoom()), (int) (10 * getZoom()), (int) (10 * getZoom()));
		getGraphics().drawRoundRect(cord.x + (int) (15 * getZoom()), cord.y + h / 2 - height + (int) (5 * getZoom()), (int) (30 * getZoom()), (int) (10 * getZoom()), (int) (10 * getZoom()), (int) (10 * getZoom()));
		int x1 = cord.x - minwidth / 4 + (int) (30 * getZoom());
		int x2 = cord.x + (int) (15 * getZoom());
		int y = cord.y + h / 2 - height / 2;
		getGraphics().drawLine(x1, y, x2, y);
		getGraphics().drawLine(x2, y, x2 - (int) (3 * getZoom()), y - (int) (2 * getZoom()));
		getGraphics().drawLine(x2, y, x2 - (int) (3 * getZoom()), y + (int) (2 * getZoom()));
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		label.setY(y - height / 2);
	}
}
