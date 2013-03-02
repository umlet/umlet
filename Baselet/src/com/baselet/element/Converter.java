package com.baselet.element;

public class Converter {

	public static java.awt.Rectangle convert(Rectangle rect) {
		return new java.awt.Rectangle(rect.x, rect.y, rect.width, rect.height);
	}

	public static Rectangle convert(java.awt.Rectangle rect) {
		return new Rectangle(rect.x, rect.y, rect.width, rect.height);
	}

	public static java.awt.Dimension convert(Dimension dim) {
		return new java.awt.Dimension(dim.width, dim.height);
	}

	public static Dimension convert(java.awt.Dimension dim) {
		return new Dimension(dim.width, dim.height);
	}
}
