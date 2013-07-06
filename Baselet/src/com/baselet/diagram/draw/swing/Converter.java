package com.baselet.diagram.draw.swing;

import java.awt.Color;

import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;

public class Converter {

	public static java.awt.Rectangle convert(Rectangle in) {
		if (in == null) return null;
		return new java.awt.Rectangle(in.x, in.y, in.width, in.height);
	}

	public static Rectangle convert(java.awt.Rectangle in) {
		if (in == null) return null;
		return new Rectangle(in.x, in.y, in.width, in.height);
	}

	public static java.awt.Dimension convert(Dimension in) {
		if (in == null) return null;
		return new java.awt.Dimension(in.width, in.height);
	}

	public static Dimension convert(java.awt.Dimension in) {
		if (in == null) return null;
		return new Dimension(in.width, in.height);
	}

	public static java.awt.Point convert(Point in) {
		if (in == null) return null;
		return new java.awt.Point(in.x, in.y);
	}

	public static Point convert(java.awt.Point in) {
		if (in == null) return null;
		return new Point(in.x, in.y);
	}

	public static ColorOwn convert(Color in) {
		if (in == null) return null;
		return new ColorOwn(in.getRed(), in.getGreen(), in.getBlue(), in.getAlpha());
	}
	
	public static Color convert(ColorOwn in) {
		if (in == null) return null;
		return new Color(in.getRed(), in.getGreen(), in.getBlue(), in.getAlpha());
	}
}
