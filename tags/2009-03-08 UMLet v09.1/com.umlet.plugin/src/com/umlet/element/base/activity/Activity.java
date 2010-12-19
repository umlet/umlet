package com.umlet.element.base.activity;

import java.awt.*;

public class Activity extends Element {

	protected Label label;
	
	public Activity(String label, Graphics2D g, String id) {
		super(g,Const.PAD,id==null?label:id);
		this.label = new Label(label,g,5);
	}
	
	@Override
	protected int getHeight() {
		return this.label.getHeight() + this.label.getPadding()*2 + super.getHeight();
	}

	@Override
	protected int getLeftWidth() {
		return this.label.getLeftWidth() + this.label.getPadding() + super.getLeftWidth();
	}

	@Override
	protected int getRightWidth() {
		return this.label.getRightWidth() + this.label.getPadding() + super.getRightWidth();
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		this.label.setY(y);
	}
	
	@Override
	public void setX(int x) {
		super.setX(x);
		this.label.setX(x);
	}

	@Override
	public void paint() {
		int h = this.getHeight();
		this.label.paint();
		this.getGraphics().drawRoundRect(this.getPosition().x - this.label.getLeftWidth() - this.label.getPadding(),
				this.getPosition().y - h/2,this.label.getWidth() + this.label.getPadding()*2,
				h, 20, 20);
	}
}
