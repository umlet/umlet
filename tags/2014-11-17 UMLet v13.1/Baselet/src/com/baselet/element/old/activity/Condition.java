package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
import com.baselet.diagram.DiagramHandler;

public class Condition extends WhileElement {

	private Label label;

	public Condition(DiagramHandler handler, String label, Graphics2D g) {
		super(handler, g, (int) (Const.PAD * handler.getZoomFactor()), null);

		setLeftWidth((int) (20 * getZoom()));
		this.label = new Label(handler, label, g, (int) (6 * getZoom()));
	}

	@Override
	public boolean arrowIn() {
		return false;
	}

	@Override
	protected int getHeight() {
		return super.getHeight() + label.getHeight() + label.getPadding() * 2;
	}

	@Override
	protected int getLeftWidth() {
		return super.getLeftWidth();
	}

	@Override
	protected int getRightWidth() {
		return label.getWidth() + super.getRightWidth() + label.getPadding() * 2;
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		label.setY(y);
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		label.setX(x + label.getLeftWidth() + label.getPadding());
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {

		Point c = getConnect(Direction.DOWN);
		getGraphics().drawLine(c.x, c.y, c.x, c.y + (int) (5 * getZoom()));
		c.y += (int) (5 * getZoom());
		return c;
	}

	@Override
	public void paint() {

		Point cord = getPosition();
		int h = getHeight();
		int width = label.getWidth() + label.getPadding() * 2;
		int height = label.getHeight() / 2 + label.getPadding();
		int pad = label.getPadding();

		// draw connector line
		getGraphics().drawLine(cord.x, cord.y - h / 2, cord.x, cord.y + h - h / 2);

		// draw left bracket
		getGraphics().drawLine(cord.x + pad / 2, cord.y - height, cord.x + pad / 2, cord.y + height);
		getGraphics().drawLine(cord.x + pad / 2, cord.y - height, cord.x + pad / 2 + (int) (5 * getZoom()), cord.y - height);
		getGraphics().drawLine(cord.x + pad / 2, cord.y + height, cord.x + pad / 2 + (int) (5 * getZoom()), cord.y + height);

		label.paint();

		// draw right bracket
		getGraphics().drawLine(cord.x + width, cord.y - height, cord.x + width, cord.y + height);
		getGraphics().drawLine(cord.x + width, cord.y - height, cord.x + width - (int) (5 * getZoom()), cord.y - height);
		getGraphics().drawLine(cord.x + width, cord.y + height, cord.x + width - (int) (5 * getZoom()), cord.y + height);
	}

}
