package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class EventRaise extends Activity {

	private int width=15;
	
	public EventRaise(Graphics2D g, String label, String id) {
		super(label,g,id==null?label:id);
		this.setRightWidth(width);
	}
	
	@Override
	public boolean connectOut_overrideable() {
		return false;
	}

	@Override
	public void paint() {
		int bh = this.getHeight()/2;
		Coordinate cord = this.getPosition();
		int uh = cord.y - (this.getHeight() - bh);
		bh += cord.y;
		this.label.paint();
		int[] xPoints = {cord.x-this.getLeftWidth(),cord.x+this.getRightWidth()-width,
				cord.x+this.getRightWidth(),cord.x+this.getRightWidth()-width,
				cord.x-this.getLeftWidth()};
		int[] yPoints = {uh,uh,cord.y,bh,bh};
		this.getGraphics().drawPolygon(xPoints, yPoints, xPoints.length);
	}

}
