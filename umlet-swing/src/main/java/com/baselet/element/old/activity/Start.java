package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;

public class Start extends Element {

	public Start(DiagramHandler handler, Graphics2D g) {
		super(handler, g, (int) (Const.PAD * handler.getZoomFactor()), null);
		setWidth((int) (21 * getZoom()));
		setHeight((int) (21 * getZoom()));
	}

	@Override
	public boolean connectIn() {
		return false;
	}

	@Override
	public void paint() {
		Point cord = getPosition();
		getGraphics().fillArc(cord.x - (int) (10 * getZoom()), cord.y - (int) (10 * getZoom()), (int) (21 * getZoom()), (int) (21 * getZoom()), 0, 360);
	}
}
