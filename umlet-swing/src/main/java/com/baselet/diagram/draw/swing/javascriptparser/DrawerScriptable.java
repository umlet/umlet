package com.baselet.diagram.draw.swing.javascriptparser;

import org.mozilla.javascript.ScriptableObject;

import com.baselet.diagram.draw.DrawHandler;

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

	@Override
	public String getClassName() {
		return getClass().getName();
	}
}
