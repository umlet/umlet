package com.umlet.element.experimental.drawable;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;

public class Text implements Drawable {

	private String text;
	private Integer x, y;
	private AlignHorizontal align;

	public Text(String text, Integer x, Integer y, AlignHorizontal align) {
		super();
		this.text = text;
		this.x = x;
		this.y = y;
		this.align = align;
	}

	public Text(String text, int y, AlignHorizontal align) {
		this(text, null, y, align);
	}

	@Override
	public void draw(BaseDrawHandler drawer) {
		if (x == null) {
			if (align == AlignHorizontal.LEFT)  drawer.printLeft(text, y);
			else if (align == AlignHorizontal.CENTER) drawer.printCenter(text, y);
			else drawer.printRight(text, y);
		}
		else drawer.print(text, x, y, align);
	}

}
