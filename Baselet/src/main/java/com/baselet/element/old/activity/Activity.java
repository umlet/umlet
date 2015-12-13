package com.baselet.element.old.activity;

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
		return label.getHeight() + label.getPadding() * 2 + super.getHeight();
	}

	@Override
	protected int getLeftWidth() {
		return label.getLeftWidth() + label.getPadding() + super.getLeftWidth();
	}

	@Override
	protected int getRightWidth() {
		return label.getRightWidth() + label.getPadding() + super.getRightWidth();
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		label.setY(y);
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		label.setX(x);
	}

	@Override
	public void paint() {
		int h = getHeight();
		label.paint();
		getGraphics().drawRoundRect(getPosition().x - label.getLeftWidth() - label.getPadding(),
				getPosition().y - h / 2, label.getWidth() + label.getPadding() * 2, h, (int) (20 * getZoom()), (int) (20 * getZoom()));
	}
}
