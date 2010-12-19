/**
 * 
 */
package com.baselet.diagram.draw;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;

class Text {
	private String text;
	private int x;
	private int y;
	private AlignHorizontal horizontalAlignment;
	private AlignVertical verticalAlignment;

	Text(String text, int x, int y, AlignHorizontal align, AlignVertical valign) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.horizontalAlignment = align;
		this.verticalAlignment = valign;
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

	public AlignVertical getVerticalAlignment() {
		return verticalAlignment;
	}
}