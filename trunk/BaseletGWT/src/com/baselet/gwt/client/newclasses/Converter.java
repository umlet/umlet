package com.baselet.gwt.client.newclasses;

import com.baselet.diagram.draw.helper.ColorOwn;
import com.google.gwt.canvas.dom.client.CssColor;

public class Converter {

	public static CssColor convert(ColorOwn color) {
		return CssColor.make("rgba(" + color.getRed() + ", " + color.getGreen() + "," + color.getBlue() + ", " + color.getAlpha()/255.0 + ")");
	}
}
