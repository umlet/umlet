package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class End extends Element {
	
	public End(Graphics2D g, String id) {
		super(g,Const.PAD,id==null?"End":id);
		this.setWidth(21);
		this.setHeight(21);
	}
	
	@Override
	public boolean connectOut_overrideable() {
		return false;
	}

	@Override
	public void paint() {
		Coordinate cord = this.getPosition();
		this.getGraphics().drawArc(cord.x-10, cord.y-10, 21, 21, 0, 360);
		this.getGraphics().fillArc(cord.x-6, cord.y-6, 13, 13, 0, 360);
	}
}
