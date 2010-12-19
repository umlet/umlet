package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class EventRecieve extends Activity {

	private int width=15;
	
	public EventRecieve(Graphics2D g, String label, String id) {
		super(label,g,id==null?label:id);
		this.setLeftWidth(width);
	}
	
	@Override
	public boolean connectIn() {
		return false;
	}
	
	@Override
	protected Coordinate getNonStdConnectOut(Direction dir) {
		if(dir.equals(Direction.LEFT)) {
			Coordinate c = this.getConnect(Direction.BOTTOM);
			Coordinate c2 = this.getConnect(Direction.LEFT);
			c.x = c2.x + width;
			this.getGraphics().drawLine(c.x, c.y, c.x, c.y+3);
			c.y += 3;
			return c;
		}
		else
			return this.getConnect(dir);
	}

	@Override
	public void paint() {
		int bh = this.getHeight()/2;
		Coordinate cord = this.getPosition();
		int uh = cord.y - (this.getHeight() - bh);
		bh += cord.y;
		this.label.paint();
		int[] xPoints = {cord.x-this.getLeftWidth(),cord.x+this.getRightWidth(),
				cord.x+this.getRightWidth(),cord.x-this.getLeftWidth(),
				cord.x-this.getLeftWidth()+width};
		int[] yPoints = {uh,uh,bh,bh,cord.y};
		this.getGraphics().drawPolygon(xPoints, yPoints, xPoints.length);
	}
}
