package com.umlet.element.experimental.drawable;

import com.baselet.diagram.draw.BaseDrawHandler;

public class Rectangle implements Drawable {

	private int x, y, width, height;
	private Float arcw, arch;
	
	public Rectangle(int x, int y, int width, int height) {
		this(x, y, width, height, null, null);
	}
	
	public Rectangle(int x, int y, int width, int height, Float arcw, Float arch) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.arcw = arcw;
		this.arch = arch;
	}

	@Override
	public void draw(BaseDrawHandler drawer) {
		if (arcw == null && arch == null) drawer.drawRectangle(x, y, width, height);
		else drawer.drawRectangleRound(x, y, width, height, arcw, arch);
	}

}
