package com.umlet.element.experimental.drawable;

import com.baselet.diagram.draw.BaseDrawHandler;

public class Rectangle implements Drawable {

	private int x, y, width, height;
	
	public Rectangle(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(BaseDrawHandler drawer) {
		drawer.drawRectangle(x, y, width, height);
	}

}
