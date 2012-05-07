package com.umlet.element.activity;

import java.awt.Graphics2D;

import com.baselet.diagram.DiagramHandler;


public class Activity extends Element {

	protected Label label;

	public Activity(DiagramHandler handler, String label, Graphics2D g, String id) {
		super(handler, g, (int) (Const.PAD * handler.getZoomFactor()), id == null ? label : id);
		this.label = new Label(handler, label, g, (int) (5 * getZoom()));
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
				this.getPosition().y - h / 2, this.label.getWidth() + this.label.getPadding() * 2, h, (int) (20 * getZoom()), (int) (20 * getZoom()));
	}
}
