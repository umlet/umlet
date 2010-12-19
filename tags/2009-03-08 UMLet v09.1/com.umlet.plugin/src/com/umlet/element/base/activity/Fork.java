package com.umlet.element.base.activity;

import java.awt.Graphics2D;

public class Fork extends StartElement {

	private int h=4;
	//pad for the connectors
	private int pad=20;
	private int w=80;
	
	//padding between connectors
	private int con_pad=10;

	private Integer current_left_x;
	private Integer current_right_x;
	
	public Fork(Graphics2D g, String id) {
		super(g, Const.PAD, id==null?"Fork":id);
		this.setHeight(h+pad);
		this.setWidth(w);
	}

	@Override
	public void paint() {
		int x = this.getPosition().x;
		int y = this.getPosition().y;
		this.getGraphics().fillRect(x-w/2, y-(h+pad)/2, w, 4);
	}
	
	@Override
	public boolean arrowIn() {
		return true;
	}
	
	@Override
	protected Coordinate getNonStdConnectIn(Direction dir) {
		Coordinate c = this.getConnect(Direction.TOP);
		
		if(dir.equals(Direction.LEFT))
			c.x -= 10;
		else if(dir.equals(Direction.RIGHT))
			c.x += 10;

		if(this.arrowIn()) 
			Connector.drawArrow(this.getGraphics(), c.x, c.y-10, c.x, c.y);
		else
			this.getGraphics().drawLine(c.x, c.y-10, c.x, c.y);
		c.y-=10;
		return c;
	}

	@Override
	protected Coordinate getNonStdConnectOut(Direction dir) {
		Coordinate c = this.getConnect(dir);
		if(dir.equals(Direction.LEFT)) {
			this.getGraphics().drawLine(c.x-10, c.y+3, c.x, c.y);
			c.x-=10;
			c.y+=3;
		}
		else if(dir.equals(Direction.RIGHT)) {
			this.getGraphics().drawLine(c.x+10, c.y+3, c.x, c.y);
			c.x+=10;
			c.y+=3;
		}

		return c;
	}

	@Override
	protected Coordinate getConnect(Direction dir) {
		Coordinate c = this.getPosition().clone();
		
		if(this.current_left_x == null) {
			this.current_left_x = this.getPosition().x;
			this.current_right_x = this.current_left_x;
		}
		
		if(dir == Direction.TOP)
			c.y -= (pad+h)/2;
		else if(dir == Direction.BOTTOM)
			c.y += (h-pad)/2;
		else if(dir == Direction.LEFT) {
			if(c.x - this.current_left_x < w/2-this.con_pad)
				this.current_left_x -= this.con_pad;
			c.x = this.current_left_x;
			c.y += (h-pad)/2;
		}
		else if(dir == Direction.RIGHT) {
			if(this.current_right_x - c.x < w/2-this.con_pad)
				this.current_right_x += this.con_pad;
			c.x = this.current_right_x;
			c.y += (h-pad)/2;
		}
		return c;
	}

	@Override
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
				
				if(from.x != to.x) {
					this.getGraphics().drawLine(from.x, from.y, to.x, to.y-Const.PAD*2);
					from.x = to.x;
					from.y = to.y-Const.PAD*2;
				}

				if(this.arrowOut() && e.arrowIn())
					Connector.drawArrow(this.getGraphics(), from.x, from.y, to.x, to.y);
				else
					this.getGraphics().drawLine(from.x, from.y, to.x, to.y);
			}
		}
	}
}
