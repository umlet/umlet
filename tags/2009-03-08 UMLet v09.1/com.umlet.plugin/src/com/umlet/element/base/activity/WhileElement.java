package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public abstract class WhileElement extends Element {

	public WhileElement(Graphics2D g, int padding, String id) {
		super(g, padding, id);
	}
	
	public void connectTo(StartElement start, StopElement stop) {
		if(this.connectOut() && start.connectIn()) {
			Coordinate from = this.getConnect(Direction.TOP);
			Coordinate to = start.getConnect(Direction.LEFT);
			
			this.getGraphics().drawLine(from.x, from.y, from.x, to.y);
			if(this.arrowOut() && start.arrowIn())
				Connector.drawArrow(this.getGraphics(), from.x, to.y, to.x, to.y);
			else
				this.getGraphics().drawLine(from.x, to.y, to.x, to.y);
		}
		
		if(this.connectIn() && stop.connectOut()) {
			Coordinate to = this.getConnect(Direction.BOTTOM);
			Coordinate from = stop.getConnect(Direction.LEFT);
			
			this.getGraphics().drawLine(from.x, from.y, to.x, from.y);
			if(this.arrowIn() && stop.arrowOut())
				Connector.drawArrow(this.getGraphics(), to.x, from.y, to.x, to.y);
			else
				this.getGraphics().drawLine(to.x, from.y, to.x, to.y);
		}
	}
}
