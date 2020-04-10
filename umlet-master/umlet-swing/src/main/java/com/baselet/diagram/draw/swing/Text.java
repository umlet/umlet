package com.baselet.diagram.draw.swing;

import com.baselet.control.StringStyle;
import com.baselet.control.enums.AlignHorizontal;

public class Text {
	private final StringStyle[] text;
	private final double x;
	private final double y;
	private final AlignHorizontal horizontalAlignment;

	public Text(StringStyle[] text, double x, double y, AlignHorizontal align) {
		this.text = text;
		this.x = x;
		this.y = y;
		horizontalAlignment = align;
	}

	public StringStyle[] getText() {
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