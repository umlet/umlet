package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class AEnd extends End {
	public AEnd(Graphics2D g, String id) {
		super(g,id==null?"AEnd":id);
	}
	
	@Override
	public void paint() {
		Coordinate cord = this.getPosition();
		this.getGraphics().drawArc(cord.x-10, cord.y-10, 21, 21, 0, 360);
		int delta = (int)(21.0/2/Math.sqrt(2));
		this.getGraphics().drawLine(cord.x-delta, cord.y-delta, cord.x+delta, cord.y+delta);
		this.getGraphics().drawLine(cord.x-delta, cord.y+delta, cord.x+delta, cord.y-delta);
	}
}
