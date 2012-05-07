package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.baselet.diagram.DiagramHandler;


public class AEnd extends End {

	public AEnd(DiagramHandler handler, Graphics2D g, String id) {
		super(handler, g, id == null ? "AEnd" : id);
	}

	@Override
	public void paint() {

		Point cord = this.getPosition();
		this.getGraphics().drawArc(cord.x - (int) (10 * getZoom()), cord.y - (int) (10 * getZoom()), (int) (21 * getZoom()), (int) (21 * getZoom()), 0, 360);
		int delta = (int) (21 * getZoom() / 2 / Math.sqrt(2));
		this.getGraphics().drawLine(cord.x - delta, cord.y - delta, cord.x + delta, cord.y + delta);
		this.getGraphics().drawLine(cord.x - delta, cord.y + delta, cord.x + delta, cord.y - delta);
	}
}
