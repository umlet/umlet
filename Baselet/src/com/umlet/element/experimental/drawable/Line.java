package com.umlet.element.experimental.drawable;

import com.baselet.diagram.draw.BaseDrawHandler;

public class Line implements Drawable {

	private int x1, y1, x2, y2;
	
	public Line(int x1, int y1, int x2, int y2) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public void draw(BaseDrawHandler drawer) {
		drawer.drawLine(x1, y1, x2, y2);
	}

}
