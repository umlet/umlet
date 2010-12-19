package com.umlet.element.base.activity;

import java.awt.*;

public class Start extends Element{

	public Start(Graphics2D g) {
		super(g,Const.PAD,null);
		this.setWidth(21);
		this.setHeight(21);
	}

	@Override
	public boolean connectIn() {
		return false;
	}

	@Override
	public void paint() {
		Coordinate cord = this.getPosition();
		this.getGraphics().fillArc(cord.x-10, cord.y-10, 
				21, 21, 0, 360);
	}
}
