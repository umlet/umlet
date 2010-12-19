package com.umlet.element.base.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.umlet.control.diagram.DiagramHandler;

public class Start extends Element {

	public Start(DiagramHandler handler, Graphics2D g) {
		super(handler, g, Const.PAD, null);
		this.setWidth((int) (21 * getZoom()));
		this.setHeight((int) (21 * getZoom()));
	}

	@Override
	public boolean connectIn() {
		return false;
	}

	@Override
	public void paint() {
		Point cord = this.getPosition();
		this.getGraphics().fillArc(cord.x - (int) (10 * getZoom()), cord.y - (int) (10 * getZoom()), (int) (21 * getZoom()), (int) (21 * getZoom()), 0, 360);
	}
}
