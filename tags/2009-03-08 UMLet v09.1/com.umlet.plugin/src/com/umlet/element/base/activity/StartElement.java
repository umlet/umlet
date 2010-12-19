package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class StartElement extends Element {
	public StartElement(Graphics2D g, int padding, String id) {
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
			if(this.connectOut_overrideable() && e.connectIn()) {
				Coordinate from = this.getPosition();
				Coordinate to = e.getConnect(Direction.TOP);
				if(from.x == to.x)
					from = this.getConnect(Direction.BOTTOM);
				else if(from.x < to.x)
					from = this.getConnect(Direction.RIGHT);
				else if(from.x > to.x)
					from = this.getConnect(Direction.LEFT);
				
				if(from.x != to.x)
					this.getGraphics().drawLine(from.x, from.y, to.x, from.y);
				if(this.arrowOut() && e.arrowIn())
					Connector.drawArrow(this.getGraphics(), to.x, from.y, to.x, to.y);
				else
					this.getGraphics().drawLine(to.x, from.y, to.x, to.y);
			}
		}
	}
}
