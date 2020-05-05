package com.baselet.control.basics;

import java.awt.Color;
import java.awt.Cursor;

import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwnBase;
import com.baselet.diagram.draw.helper.ColorOwnLight;
import com.baselet.element.interfaces.CursorOwn;

public class Converter {

	public static java.awt.Rectangle convert(Rectangle in) {
		if (in == null) {
			return null;
		}
		return new java.awt.Rectangle(in.x, in.y, in.width, in.height);
	}

	public static Rectangle convert(java.awt.Rectangle in) {
		if (in == null) {
			return null;
		}
		return new Rectangle(in.x, in.y, in.width, in.height);
	}

	public static java.awt.Dimension convert(Dimension in) {
		if (in == null) {
			return null;
		}
		return new java.awt.Dimension(in.width, in.height);
	}

	public static Dimension convert(java.awt.Dimension in) {
		if (in == null) {
			return null;
		}
		return new Dimension(in.width, in.height);
	}

	public static java.awt.Point convert(Point in) {
		if (in == null) {
			return null;
		}
		return new java.awt.Point(in.x, in.y);
	}

	public static Point convert(java.awt.Point in) {
		if (in == null) {
			return null;
		}
		return new Point(in.x, in.y);
	}

	public static ColorOwnBase convert(Color in) {
		if (in == null) {
			return null;
		}
		// TODO: Think about how to handle this case
		return new ColorOwnLight(in.getRed(), in.getGreen(), in.getBlue(), in.getAlpha());
	}

	public static Color convert(ColorOwnBase in) {
		if (in == null) {
			return null;
		}
		return new Color(in.getRed(), in.getGreen(), in.getBlue(), in.getAlpha());
	}

	public static Cursor convert(CursorOwn in) {
		switch (in) {
			case N:
				return new Cursor(Cursor.N_RESIZE_CURSOR);
			case NE:
				return new Cursor(Cursor.NE_RESIZE_CURSOR);
			case E:
				return new Cursor(Cursor.E_RESIZE_CURSOR);
			case SE:
				return new Cursor(Cursor.SE_RESIZE_CURSOR);
			case S:
				return new Cursor(Cursor.S_RESIZE_CURSOR);
			case SW:
				return new Cursor(Cursor.SW_RESIZE_CURSOR);
			case W:
				return new Cursor(Cursor.W_RESIZE_CURSOR);
			case NW:
				return new Cursor(Cursor.NW_RESIZE_CURSOR);
			case HAND:
				return new Cursor(Cursor.HAND_CURSOR);
			case MOVE:
				return new Cursor(Cursor.MOVE_CURSOR);
			case DEFAULT:
				return new Cursor(Cursor.DEFAULT_CURSOR);
			case CROSS:
				return new Cursor(Cursor.CROSSHAIR_CURSOR);
			case TEXT:
				return new Cursor(Cursor.TEXT_CURSOR);
			default:
				throw new RuntimeException("Unknown Cursor: " + in);
		}
	}
}
