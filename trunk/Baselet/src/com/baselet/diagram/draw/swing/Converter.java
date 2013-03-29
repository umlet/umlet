package com.baselet.diagram.draw.swing;

import java.awt.Color;

import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;

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

	public static java.awt.Point convert(Point p) {
		return new java.awt.Point(p.x, p.y);
	}

	public static Point convert(java.awt.Point p) {
		return new Point(p.x, p.y);
	}

	public static ColorOwn convert(Color color) {
		return new ColorOwn(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public static Color convert(ColorOwn color) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}
}
