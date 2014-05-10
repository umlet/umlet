package com.baselet.gwt.client;

import com.baselet.diagram.draw.helper.ColorOwn;
import com.google.gwt.canvas.dom.client.CssColor;

public class Converter {

	public static CssColor convert(ColorOwn in) {
		if (in == null) {
			return null;
		}
		return CssColor.make("rgba(" + in.getRed() + ", " + in.getGreen() + "," + in.getBlue() + ", " + in.getAlpha() / 255.0 + ")");
	}
}
