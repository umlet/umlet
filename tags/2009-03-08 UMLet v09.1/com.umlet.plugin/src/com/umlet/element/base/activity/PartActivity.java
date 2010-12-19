package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class PartActivity extends Activity {

	private int minwidth = 120;
	private int height = 20;
	
	public PartActivity(String label, Graphics2D g, String id) {
		super(label, g, id==null?label:id);
		this.setHeight(height);
	}

	@Override
	protected int getLeftWidth() {
		return super.getLeftWidth() > minwidth/2 ? super.getLeftWidth() : minwidth/2;
	}

	@Override
	protected int getRightWidth() {
		return super.getRightWidth()  > minwidth/2 ? super.getRightWidth() : minwidth/2;
	}

	@Override
	public void paint() {
		Coordinate cord = this.getPosition();
		int h = this.getHeight();
		this.label.paint();
		this.getGraphics().drawRoundRect(cord.x - this.getLeftWidth(),
				cord.y - h/2,this.getWidth(),
				h, 20, 20);
		this.getGraphics().drawRoundRect(cord.x-minwidth/4, cord.y + h/2 - height + 5, 
				30, 10, 10, 10);
		this.getGraphics().drawRoundRect(cord.x+15, cord.y + h/2 - height + 5, 
				30, 10, 10, 10);
		int x1=cord.x-minwidth/4+30;
		int x2=cord.x+15;
		int y=cord.y+h/2-height/2;
		this.getGraphics().drawLine(x1, y,x2,y);
		this.getGraphics().drawLine(x2,y,x2-3,y-2);
		this.getGraphics().drawLine(x2,y,x2-3,y+2);
	}
	
	@Override
	public void setY(int y) {
		super.setY(y);
		this.label.setY(y-height/2);
	}
}
