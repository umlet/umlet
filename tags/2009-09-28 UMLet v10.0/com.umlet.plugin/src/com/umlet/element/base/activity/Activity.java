package com.umlet.element.base.activity;

import java.awt.Graphics2D;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;

public class Activity extends Element {

	private float zoom = (float) Umlet.getInstance().getDiagramHandler().getGridSize() / (float) Constants.defaultGridSize;
	protected Label label;

	public Activity(String label, Graphics2D g, String id) {
		super(g, Const.PAD, id == null ? label : id);
		this.label = new Label(label, g, (int) (5 * zoom));
	}

	@Override
	protected int getHeight() {
		return this.label.getHeight() + this.label.getPadding() * 2 + super.getHeight();
	}

	@Override
	protected int getLeftWidth() {
		return this.label.getLeftWidth() + this.label.getPadding() + super.getLeftWidth();
	}

	@Override
	protected int getRightWidth() {
		return this.label.getRightWidth() + this.label.getPadding() + super.getRightWidth();
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		this.label.setY(y);
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		this.label.setX(x);
	}

	@Override
	public void paint() {
		int h = this.getHeight();
		this.label.paint();
		this.getGraphics().drawRoundRect(this.getPosition().x - this.label.getLeftWidth() - this.label.getPadding(),
				this.getPosition().y - h / 2, this.label.getWidth() + this.label.getPadding() * 2, h, (int) (20 * zoom), (int) (20 * zoom));
	}
}
