package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class StopElement extends Element {

	public StopElement(Graphics2D g, int padding, String id) {
		super(g, padding, id);
	}

	@Override
	public void paint() {
		
	}

	@Override
	public boolean arrowIn() {
		return false;
	}
	
	public void connectTo(Element e) {
		if(e != null) {
			if(this.connectIn() && e.connectOut()) {
				Coordinate to = this.getPosition();
				Coordinate from = e.getConnect(Direction.BOTTOM);
				if(from.x == to.x)
					to = this.getConnect(Direction.TOP);
				else if(from.x < to.x)
					to = this.getConnect(Direction.LEFT);
				else if(from.x > to.x)
					to = this.getConnect(Direction.RIGHT);
				
				if(from.x != to.x) {
					this.getGraphics().drawLine(from.x, from.y, from.x, to.y);
					if(this.arrowIn() && e.arrowOut())
						Connector.drawArrow(this.getGraphics(), from.x, to.y, to.x, to.y);
					else
						this.getGraphics().drawLine(from.x, to.y, to.x, to.y);
				}
				else {
					if(this.arrowIn() && e.arrowOut())
						Connector.drawArrow(this.getGraphics(), from.x, from.y, to.x, to.y);
					else
						this.getGraphics().drawLine(from.x, from.y, to.x, to.y);
				}
			}
		}
	}
}
