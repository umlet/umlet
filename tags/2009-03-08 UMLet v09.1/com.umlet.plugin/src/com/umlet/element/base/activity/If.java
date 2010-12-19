package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class If extends StartElement {

	private int h=40;
	private int w=40;
	
	public If(Graphics2D g, String id) {
		super(g, Const.PAD, id==null?"If":id);
		this.setHeight(h);
		this.setWidth(w);
	}

	@Override
	public void paint() {
		int x = this.getPosition().x;
		int y = this.getPosition().y;
		this.getGraphics().drawLine(x, y-h/2, x+w/2, y);
		this.getGraphics().drawLine(x, y-h/2, x-w/2, y);
		this.getGraphics().drawLine(x, y+h/2, x+w/2, y);
		this.getGraphics().drawLine(x, y+h/2, x-w/2, y);
	}
	
	@Override
	public boolean arrowIn() {
		return true;
	}
}
