package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public class Condition extends WhileElement {

	private Label label;

	public Condition(DiagramHandler handler, String label, Graphics2D g) {
		super(handler, g, (int) (Const.PAD * handler.getZoomFactor()), null);

		this.setLeftWidth((int) (20 * getZoom()));
		this.label = new Label(handler, label, g, (int) (6 * getZoom()));
	}

	@Override
	public boolean arrowIn() {
		return false;
	}

	@Override
	protected int getHeight() {
		return super.getHeight() + this.label.getHeight() + this.label.getPadding() * 2;
	}

	@Override
	protected int getLeftWidth() {
		return super.getLeftWidth();
	}

	@Override
	protected int getRightWidth() {
		return this.label.getWidth() + super.getRightWidth() + this.label.getPadding() * 2;
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		this.label.setY(y);
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		this.label.setX(x + label.getLeftWidth() + label.getPadding());
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {

		Point c = this.getConnect(Direction.BOTTOM);
		this.getGraphics().drawLine(c.x, c.y, c.x, c.y + (int) (5 * getZoom()));
		c.y += (int) (5 * getZoom());
		return c;
	}

	@Override
	public void paint() {

		Point cord = this.getPosition();
		int h = this.getHeight();
		int width = this.label.getWidth() + this.label.getPadding() * 2;
		int height = this.label.getHeight() / 2 + this.label.getPadding();
		int pad = this.label.getPadding();

		// draw connector line
		this.getGraphics().drawLine(cord.x, cord.y - h / 2, cord.x, cord.y + h - h / 2);

		// draw left bracket
		this.getGraphics().drawLine(cord.x + pad / 2, cord.y - height, cord.x + pad / 2, cord.y + height);
		this.getGraphics().drawLine(cord.x + pad / 2, cord.y - height, cord.x + pad / 2 + (int) (5 * getZoom()), cord.y - height);
		this.getGraphics().drawLine(cord.x + pad / 2, cord.y + height, cord.x + pad / 2 + (int) (5 * getZoom()), cord.y + height);

		this.label.paint();

		// draw right bracket
		this.getGraphics().drawLine(cord.x + width, cord.y - height, cord.x + width, cord.y + height);
		this.getGraphics().drawLine(cord.x + width, cord.y - height, cord.x + width - (int) (5 * getZoom()), cord.y - height);
		this.getGraphics().drawLine(cord.x + width, cord.y + height, cord.x + width - (int) (5 * getZoom()), cord.y + height);
	}

}
