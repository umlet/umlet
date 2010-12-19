package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class Condition extends WhileElement {
	private Label label;
	
	public Condition(String label, Graphics2D g) {
		super(g,Const.PAD,null);
		this.setLeftWidth(20);
		this.label = new Label(label,g,6);
	}
	
	@Override
	public boolean arrowIn() {
		return false;
	}

	@Override
	protected int getHeight() {
		return super.getHeight() + this.label.getHeight() + this.label.getPadding()*2;
	}

	@Override
	protected int getLeftWidth() {
		return super.getLeftWidth();
	}

	@Override
	protected int getRightWidth() {
		return this.label.getWidth() + super.getRightWidth() + this.label.getPadding()*2;
	}
	
	@Override
	public void setY(int y) {
		super.setY(y);
		this.label.setY(y);
	}
	
	@Override
	public void setX(int x) {
		super.setX(x);
		this.label.setX(x + label.getLeftWidth() + label.getPadding());
	}
	
	@Override
	protected Coordinate getNonStdConnectOut(Direction dir) {
		Coordinate c = this.getConnect(Direction.BOTTOM);
		this.getGraphics().drawLine(c.x, c.y, c.x, c.y+5);
		c.y += 5;
		return c;
	}

	@Override
	public void paint() {
		Coordinate cord = this.getPosition();
		int h = this.getHeight();
		int width = this.label.getWidth() + this.label.getPadding()*2;
		int height = this.label.getHeight()/2 + this.label.getPadding();
		int pad = this.label.getPadding();
		
		//draw connector line
		this.getGraphics().drawLine(cord.x,cord.y-h/2,cord.x,cord.y+h-h/2);	
		
		//draw left bracket
		this.getGraphics().drawLine(cord.x + pad/2, cord.y-height, cord.x + pad/2, cord.y + height);
		this.getGraphics().drawLine(cord.x + pad/2, cord.y-height, cord.x + pad/2+5, cord.y-height);
		this.getGraphics().drawLine(cord.x + pad/2, cord.y+height, cord.x + pad/2+5, cord.y+height);
		
		this.label.paint();
		
		//draw right bracket
		this.getGraphics().drawLine(cord.x + width, cord.y-height, cord.x + width, cord.y + height);
		this.getGraphics().drawLine(cord.x + width, cord.y-height, cord.x + width - 5, cord.y-height);
		this.getGraphics().drawLine(cord.x + width, cord.y+height, cord.x + width - 5, cord.y+height);
	}
	
	
}
