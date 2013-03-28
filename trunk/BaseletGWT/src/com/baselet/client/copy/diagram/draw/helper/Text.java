
package com.baselet.client.copy.diagram.draw.helper;

import com.baselet.client.copy.control.enumerations.AlignHorizontal;

public class Text {
	private String text;
	private float x;
	private float y;
	private AlignHorizontal horizontalAlignment;

	public Text(String text, float x, float y, AlignHorizontal align) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.horizontalAlignment = align;
	}

	public String getText() {
		return text;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public AlignHorizontal getHorizontalAlignment() {
		return horizontalAlignment;
	}
}