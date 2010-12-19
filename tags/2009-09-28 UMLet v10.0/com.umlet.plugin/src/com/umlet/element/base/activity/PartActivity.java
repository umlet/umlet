package com.umlet.element.base.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;

public class PartActivity extends Activity {

	private float zoom = (float) Umlet.getInstance().getDiagramHandler().getGridSize() / (float) Constants.defaultGridSize;

	private int minwidth = (int) (120 * zoom);
	private int height = (int) (20 * zoom);

	public PartActivity(String label, Graphics2D g, String id) {
		super(label, g, id == null ? label : id);
		this.setHeight(height);
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
		Point cord = this.getPosition();
		int h = this.getHeight();
		this.label.paint();
		this.getGraphics().drawRoundRect(cord.x - this.getLeftWidth(), cord.y - h / 2, this.getWidth(), h, (int) (20 * zoom), (int) (20 * zoom));
		this.getGraphics().drawRoundRect(cord.x - minwidth / 4, cord.y + h / 2 - height + (int) (5 * zoom), (int) (30 * zoom), (int) (10 * zoom), (int) (10 * zoom), (int) (10 * zoom));
		this.getGraphics().drawRoundRect(cord.x + (int) (15 * zoom), cord.y + h / 2 - height + (int) (5 * zoom), (int) (30 * zoom), (int) (10 * zoom), (int) (10 * zoom), (int) (10 * zoom));
		int x1 = cord.x - minwidth / 4 + (int) (30 * zoom);
		int x2 = cord.x + (int) (15 * zoom);
		int y = cord.y + h / 2 - height / 2;
		this.getGraphics().drawLine(x1, y, x2, y);
		this.getGraphics().drawLine(x2, y, x2 - (int) (3 * zoom), y - (int) (2 * zoom));
		this.getGraphics().drawLine(x2, y, x2 - (int) (3 * zoom), y + (int) (2 * zoom));
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		this.label.setY(y - height / 2);
	}
}
