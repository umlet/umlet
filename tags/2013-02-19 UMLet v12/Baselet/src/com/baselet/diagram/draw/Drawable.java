package com.baselet.diagram.draw;

import java.awt.Shape;

public class Drawable {
	
	private Style style;
	private Text text;
	private Shape shape;
	
	public Drawable(Style style, Text text) {
		super();
		this.style = style;
		this.text = text;
	}
	
	public Drawable(Style style, Shape shape) {
		super();
		this.style = style;
		this.shape = shape;
	}

	public Style getStyle() {
		return style;
	}

	public Text getText() {
		return text;
	}

	public Shape getShape() {
		return shape;
	}
	
}
