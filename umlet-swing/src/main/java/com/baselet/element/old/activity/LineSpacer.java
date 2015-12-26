package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.control.enums.Direction;
import com.baselet.diagram.DiagramHandler;

public class LineSpacer extends WhileElement {

	public LineSpacer(DiagramHandler handler, Graphics2D g) {
		super(handler, g, 0, null);
		setHeight((int) (Const.PAD * 2 * getZoom()));
		setWidth((int) (Const.PAD * 6 * getZoom()));
	}

	@Override
	public boolean arrowIn() {
		return false;
	}

	@Override
	protected Point getNonStdConnectOut(Direction dir) {
		return getConnect(Direction.DOWN);
	}

	@Override
	public void paint() {
		int h = getHeight() / 2;
		Point cord = getPosition();
		getGraphics().drawLine(cord.x, cord.y - h, cord.x, cord.y + h);
	}
}
