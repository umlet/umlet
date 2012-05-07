package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public class LineSpacer extends WhileElement {

	public LineSpacer(DiagramHandler handler, Graphics2D g) {
		super(handler, g, 0, null);
		this.setHeight((int) (Const.PAD * 2 * getZoom()));
		this.setWidth((int) (Const.PAD * 6 * getZoom()));
	}

	@Override
	public boolean arrowIn() {
		return false;
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {
		return this.getConnect(Direction.BOTTOM);
	}

	@Override
	public void paint() {
		int h = this.getHeight() / 2;
		Point cord = this.getPosition();
		this.getGraphics().drawLine(cord.x, cord.y - h, cord.x, cord.y + h);
	}
}
