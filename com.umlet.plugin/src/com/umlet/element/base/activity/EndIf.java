package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class EndIf extends StopElement {

	private int h=40;
	private int w=40;
	
	public EndIf(Graphics2D g, String id) {
		super(g, Const.PAD, id==null?"EndIf":id);
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
