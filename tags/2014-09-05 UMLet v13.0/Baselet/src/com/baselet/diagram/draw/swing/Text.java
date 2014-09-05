package com.baselet.diagram.draw.swing;

import com.baselet.control.enumerations.AlignHorizontal;

public class Text {
	private String text;
	private double x;
	private double y;
	private AlignHorizontal horizontalAlignment;

	public Text(String text, double x, double y, AlignHorizontal align) {
		this.text = text;
		this.x = x;
		this.y = y;
		horizontalAlignment = align;
	}

	public String getText() {
		return text;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public AlignHorizontal getHorizontalAlignment() {
		return horizontalAlignment;
	}
}