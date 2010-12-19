package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class LineSpacer extends WhileElement {

	private int height = Const.PAD*2;
	
	public LineSpacer(Graphics2D g) {
		super(g, 0,null);
		this.setHeight(height);
		this.setWidth(Const.PAD*6);
	}

	@Override
	public boolean arrowIn() {
		return false;
	}

	@Override
	protected Coordinate getNonStdConnectOut(Direction dir) {
		return this.getConnect(Direction.BOTTOM);
	}

	@Override
	public void paint() {
		int h = this.getHeight()/2;
		Coordinate cord = this.getPosition();
		this.getGraphics().drawLine(cord.x, cord.y-h, cord.x, cord.y+h);
	}
}
