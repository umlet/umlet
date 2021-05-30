package com.baselet.diagram.draw;

public abstract class JavascriptCodeParser {
	private DrawHandler drawer;

	public abstract void parse(String line, int width, int height);

	public DrawHandler getDrawer() {
		return drawer;
	}

	public void setDrawer(DrawHandler drawer) {
		this.drawer = drawer;
	}
}