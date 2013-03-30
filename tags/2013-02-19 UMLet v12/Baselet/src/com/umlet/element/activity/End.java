package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public class End extends Element {

	public End(DiagramHandler handler, Graphics2D g, String id) {
		super(handler, g, (int) (Const.PAD * handler.getZoomFactor()), id == null ? "End" : id);
		this.setWidth((int) (21 * getZoom()));
		this.setHeight((int) (21 * getZoom()));
	}

	@Override
	public boolean connectOut_overrideable() {
		return false;
	}

	@Override
	public void paint() {

		Point cord = this.getPosition();
		this.getGraphics().drawArc(cord.x - (int) (10 * getZoom()), cord.y - (int) (10 * getZoom()), (int) (21 * getZoom()), (int) (21 * getZoom()), 0, 360);
		this.getGraphics().fillArc(cord.x - (int) (6 * getZoom()), cord.y - (int) (6 * getZoom()), (int) (13 * getZoom()), (int) (13 * getZoom()), 0, 360);
	}
}
