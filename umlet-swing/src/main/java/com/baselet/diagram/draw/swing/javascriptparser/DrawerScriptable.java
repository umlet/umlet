package com.baselet.diagram.draw.swing.javascriptparser;

import org.mozilla.javascript.ScriptableObject;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.StyleException;

@SuppressWarnings("serial")
public class DrawerScriptable extends ScriptableObject {
	DrawHandler drawer;

	public DrawerScriptable(DrawHandler drawer) {
		this.drawer = drawer;
	}

	public void drawCircle(double x, double y, double radius) {
		drawer.drawCircle(x, y, radius);
	}

	public void drawRectangle(double x, double y, double width, double height) {
		drawer.drawRectangle(x, y, width, height);
	}

	public void drawLine(double x1, double y1, double x2, double y2) {
		drawer.drawLine(x1, y1, x2, y2);
	}

	public void drawArc(double x, double y, double width, double height, double start, double extent, boolean open) {
		drawer.drawArc(x, y, width, height, start, extent, open);
	}

	public void drawEllipse(double x, double y, double width, double height) {
		drawer.drawEllipse(x, y, width, height);
	}

	public void drawRectangleRound(double x, double y, double width, double height, double radius) {
		drawer.drawRectangleRound(x, y, width, height, radius);
	}

	public void drawText(String multiLineWithMarkup, double x, double y, String align) {
		AlignHorizontal alignHorizontal = getAlignHorizontalEnumFromString(align);
		drawer.print(multiLineWithMarkup, x, y, alignHorizontal);
	}

	private AlignHorizontal getAlignHorizontalEnumFromString(String align) {
		for (AlignHorizontal alignHorizontal : AlignHorizontal.values()) {
			if (alignHorizontal.toString().equalsIgnoreCase(align)) {
				return alignHorizontal;
			}
		}
		throw new StyleException("Allowed values for AlignHorizontal: center, left, right");
	}

	@Override
	public String getClassName() {
		return getClass().getName();
	}
}
