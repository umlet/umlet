package com.umlet.element.experimental.facet;

import java.util.HashSet;
import java.util.Set;

import com.baselet.control.Utils;
import com.baselet.control.Constants.AlignHorizontal;
import com.umlet.element.experimental.drawable.Drawable;
import com.umlet.element.experimental.drawable.Line;
import com.umlet.element.experimental.drawable.Text;

public class Facets {
	
	public static Set<Drawable> getClassDrawables(String panelText, int lineHeight, int width) {
		Set<Drawable> drawables = new HashSet<Drawable>();
		AlignHorizontal align = AlignHorizontal.CENTER;
		float distanceBetweenTexts = lineHeight;
		int yPos = (int) distanceBetweenTexts;
		for (String line : Utils.decomposeStrings(panelText)) {
			if (line.equals("--")) {
				align = AlignHorizontal.LEFT;
				int linePos = (int) (yPos - (distanceBetweenTexts/2));
				drawables.add(new Line(0, linePos, width, linePos));
			}
			else {
				drawables.add(new Text(line, yPos, align));
			}
			yPos += (int) distanceBetweenTexts;
		}
		return drawables;
	}
}
