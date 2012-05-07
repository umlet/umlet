/**
 * 
 */
package com.baselet.diagram.draw;

import com.baselet.control.Constants.AlignHorizontal;

class Text {
	private String text;
	private int x;
	private int y;
	private AlignHorizontal horizontalAlignment;

	Text(String text, int x, int y, AlignHorizontal align) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.horizontalAlignment = align;
	}

	public String getText() {
		return text;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public AlignHorizontal getHorizontalAlignment() {
		return horizontalAlignment;
	}
}