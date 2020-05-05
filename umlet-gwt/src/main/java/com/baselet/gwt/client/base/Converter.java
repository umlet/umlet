package com.baselet.gwt.client.base;

import com.baselet.diagram.draw.helper.ColorOwnBase;
import com.baselet.element.interfaces.CursorOwn;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style;

public class Converter {

	public static CssColor convert(ColorOwnBase in) {
		if (in == null) {
			return null;
		}
		return CssColor.make("rgba(" + in.getRed() + ", " + in.getGreen() + "," + in.getBlue() + ", " + in.getAlpha() / 255.0 + ")");
	}

	public static Style.Cursor convert(CursorOwn in) {
		switch (in) {
			case N:
				return Style.Cursor.N_RESIZE;
			case NE:
				return Style.Cursor.NE_RESIZE;
			case E:
				return Style.Cursor.E_RESIZE;
			case SE:
				return Style.Cursor.SE_RESIZE;
			case S:
				return Style.Cursor.S_RESIZE;
			case SW:
				return Style.Cursor.SW_RESIZE;
			case W:
				return Style.Cursor.W_RESIZE;
			case NW:
				return Style.Cursor.NW_RESIZE;
			case HAND:
				return Style.Cursor.POINTER;
			case MOVE:
				return Style.Cursor.MOVE;
			case DEFAULT:
				return Style.Cursor.DEFAULT;
			case CROSS:
				return Style.Cursor.CROSSHAIR;
			case TEXT:
				return Style.Cursor.TEXT;
			default:
				throw new RuntimeException("Unknown Cursor: " + in);
		}
	}
}
